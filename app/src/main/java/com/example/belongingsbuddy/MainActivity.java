package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements Listener, TagListener{
    private ArrayList<Item> dataList;
    private ArrayList<Item> originalOrderDataList;
    private ListView itemListView;
    private ArrayAdapter<Item> itemAdapter;
    private TextView totalTextView;
    private FirebaseFirestore db;
    private CollectionReference user_collection;
    private String username;
    private LinearLayout sortTypeLayout;
    private TextView sortTypeTextView;
    private LinearLayout filterTypeLayout;
    private TextView filterTypeTextView;
    private FirebaseUser user;

    private TagManager tagManager;
    public final static int REQUEST_CODE_ADD = 1;
    public final static int REQUEST_CODE_VIEW = 2;
    public final static int REQUEST_CODE_EDIT = 3;
    public final static int REQUEST_CODE_BARCODE = 10;
    public static Integer lastResult = 1000;

    /**
     * Prompt user to Login or Signup.
     * Load user's Items and other data from their FireStore collection.
     * Set onClickListeners for all necessary buttons
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // access username button
        final Button username_button = findViewById(R.id.username);
        // initialize username variable with placeholder value
        username = "test";

        // receive intent from Login Activity activity switch
        Intent intent = getIntent();
        // get authorization instance to get email of current user
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            username = auth.getCurrentUser().getEmail().split("@")[0];
        } else {
            // testing user
            auth.signInWithEmailAndPassword(getString(R.string.test_email), getString(R.string.test_password))
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGNIN", "signInWithEmail:success");
                                user = auth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGNIN", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }
        username_button.setText(username);

        // create collection for current user (I setup firestore rules to only allow users to edit their own collections)
        db = FirebaseFirestore.getInstance();

        String uID = "";
        if (auth.getCurrentUser() != null) {
            uID = auth.getCurrentUser().getUid();
            user_collection = db.collection(uID); // collection name MUST be the FirestoreAuth uID

            }


        // First: set up dataList, itemListView, and itemAdapter
        dataList = new ArrayList<Item>();
        itemListView = findViewById(R.id.item_list);

        // setup dataList copy
        // since copy is in onCreate, user can forget to clear prev sort and it will rollback properly
        originalOrderDataList = new ArrayList<Item>();
        originalOrderDataList.addAll(dataList);

        // set up itemAdapter and itemListView
        itemAdapter = new CustomList(this, dataList);
        itemListView.setAdapter(itemAdapter);

        // initialize tag manager
        tagManager = new TagManager();

        totalTextView = findViewById(R.id.total);
        // LOAD Items from user's collection on FireStore and add those items to dataList
        if (user_collection != null) {
            user_collection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                    @Nullable FirebaseFirestoreException error) {
                    if (error != null){
                        Log.e("Firestore", error.toString());
                        return;
                    }
                    if (querySnapshots != null){
                        dataList.clear();
                        for (QueryDocumentSnapshot doc: querySnapshots) {
                            String id = doc.getId();
                            Log.d("docid", id);
                            if (id.equals("userTags")) {
                                List<Map<String, Object>> tagMaps = (List<Map<String, Object>>) doc.get("tags");
                                tagManager.setTags(tagManager.convertTagDatamap(tagMaps));
                            } else {
                                String comment = (String) doc.get("comment");
                                int day = ((Long) doc.get("day")).intValue();
                                int month = ((Long) doc.get("month")).intValue();
                                int year = ((Long) doc.get("year")).intValue();
                                Date date = new Date(day, month, year);
                                String description = (String) doc.get("description");
                                Float estimatedValue = ((Double) doc.get("estimatedValue")).floatValue();
                                String make = (String) doc.get("make");
                                String model = (String) doc.get("model");
                                String name = (String) doc.get("name");
                                List<String> photoURLs = (List<String>) doc.get("photoURLs");
                                ArrayList<Photo> photos = (ArrayList<Photo>) doc.get("photos");
                                String serialNumber = (String) doc.get("serialNumber");
                                List<Map<String, Object>> tags = (List<Map<String, Object>>) doc.get("tags");
                                String epoch = (String) doc.get("epoch");
                                Integer quantity = ((Long) doc.get("quantity")).intValue();
                                Item item = new Item(name, date, description, make, model, estimatedValue, comment, serialNumber, photos, epoch, id, quantity, photoURLs);
                                tagManager.setItemTags(item, tagManager.convertTagDatamap(tags));
                                if (item.getPhotoURLs() != null) {
                                    if (item.getPhotoURLs().size() > 0) {
                                        Log.d("PHOTO URLS", item.getPhotoURLs().get(0));
                                    }
                                }
                                dataList.add(item);
                            }
                            itemAdapter.notifyDataSetChanged();
    
                            // setup backup
                            originalOrderDataList.clear();
                            originalOrderDataList.addAll(dataList);
    
                            // set total
                            totalTextView.setText(String.format("$%.2f", sumItems(dataList)));
                        }
                    }
                }
            });
        }

        // get ui objects for sort
        sortTypeLayout = findViewById(R.id.sort_type_layout);
        sortTypeTextView = findViewById(R.id.sort_type_textview);

        // get ui objects for filter
        filterTypeLayout = findViewById(R.id.filter_type_layout);
//        filterTypeTextView = findViewById(R.id.filter_type_textview);

        // click listener for items in ListView
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
/**
             * When an Item form the AdapterView is clicked on, system gets information about the
             * Item and starts a ItemViewActivity passing the Item's data as Extras
             * @param parent The AdapterView where the click happened.
             * @param view The view within the AdapterView that was clicked (this
             *            will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id The row id of the item that was clicked.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if sort not closed
                if (sortTypeLayout.getVisibility() == View.VISIBLE) {
                    cleanupSort();
                }
                // if filter already pressed redo filter
                if (filterTypeLayout.getVisibility() == View.VISIBLE) {
                    cleanupFilter();
                }
                // get the Item being clicked
                Item i = itemAdapter.getItem(position);
                //Log.d("tag", i.getEpoch());
                // setup the ItemViewActivity by creating a new Intent and passing Item data as extras
                Intent intent = new Intent(MainActivity.this, ItemViewActivity.class);
                intent.putExtra("name", i.getName());
                intent.putExtra("date", i.getDate().getString());
                intent.putExtra("description", i.getDescription());
                intent.putExtra("make", i.getMake());
                intent.putExtra("model", i.getModel());
                intent.putExtra("value", i.getEstimatedValue());
                intent.putExtra("serialNum", i.getSerialNumber());
                intent.putExtra("quantity", i.getQuantity());
                intent.putExtra("comment", i.getComment());
                intent.putExtra("index", position);
                intent.putExtra("manager", tagManager);
                intent.putExtra("tagsList", new ArrayList<>(tagManager.getItemTags(i)));
                intent.putExtra("tagsString", tagManager.printItemTags(i, false));
                if (i.getPhotoURLs() != null) {
                    intent.putExtra("photoURLsize", i.getPhotoURLs().size());
                    for (int j = 0; j <i.getPhotoURLs().size(); j++) {
                        intent.putExtra("photoURL"+j, i.getPhotoURLs().get(j));
                    }
                };
                startActivityForResult(intent, REQUEST_CODE_VIEW);
            }
        });

        // ADD ITEM implementation:
        final Button addButton = findViewById(R.id.add_item);
        addButton.setOnClickListener(v -> {
            // if sort not closed
            if (sortTypeLayout.getVisibility() == View.VISIBLE) {
                cleanupSort();
            }
            // if filter already pressed redo filter
            if (filterTypeLayout.getVisibility() == View.VISIBLE) {
                cleanupFilter();
            }
            ScanOrManual dialog = new ScanOrManual();
            dialog.show(getSupportFragmentManager(), "Add Item:");
        });

        // click listener sort type rollback
        final Button rollbackSort = findViewById(R.id.sort_type_rollback);
        rollbackSort.setOnClickListener(v -> {
            // hide selected sorts
            sortTypeLayout.setVisibility(View.GONE);
            // rollback to original sort ordering
            dataList.clear();
            dataList.addAll(originalOrderDataList);
            itemAdapter.notifyDataSetChanged();
        });

        // click listener for sort:
        final Button sortButton = findViewById(R.id.sort_button);
        sortButton.setOnClickListener(v -> {
            new SortItemsFragment().show(getSupportFragmentManager(), "Sort Items:");
        });

        // view filter click listener
        final Button viewFilterButton = findViewById(R.id.filter_type_rollback);
        viewFilterButton.setOnClickListener(v -> {
            new ViewFilterFragment().show(getSupportFragmentManager(), "View Filter:");
        });

        // click listener filter rollback
        final Button rollbackFilter = findViewById(R.id.filter_type_rollback);
        rollbackFilter.setOnClickListener(v -> {
            // hide selected filters
            filterTypeLayout.setVisibility(View.GONE);
            // rollback to original sort ordering
            dataList.clear();
            dataList.addAll(originalOrderDataList);
            itemAdapter.notifyDataSetChanged();
        });

        // click listener for filter:
        final Button filterButton = findViewById(R.id.filter_button);
        filterButton.setOnClickListener(v -> {
            new FilterItemsFragment().show(getSupportFragmentManager(), "Filter Item:");
        });

        // click listener for tag creation
        final Button tagButton = findViewById(R.id.tag_button);
        tagButton.setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putSerializable("Manager", tagManager);
            CreateTagActivity TagFragment = new CreateTagActivity();
            TagFragment.setArguments(arg);
            TagFragment.show(getSupportFragmentManager(), "createTagDialog");
        });

        // create dialog from clicking username
        username_button.setOnClickListener(new View.OnClickListener() {
            /**
             * opens the username dialog for signout
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // passes username to dialog
                Bundle arg = new Bundle();
                arg.putString("username", username);
                DialogFragment newFragment = new UsernameDialogFragment();
                newFragment.setArguments(arg);
                newFragment.show(getSupportFragmentManager(), "User Control");
            }
        });

        // Set long-click listener to enter multi-select mode
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((CustomList) itemAdapter).setMultiSelectMode(true); // Enter multi-select mode
                hideMultiSelectButtons(); // Hide multi-select buttons


                // Set an onClickListener for the delete button
                return true;
            }
        });

        final Button selectAllButton = findViewById(R.id.select_all_button);
        selectAllButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Selects all items in ListView and moves to MultiSelectMode
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                ((CustomList) itemAdapter).setMultiSelectMode(true);
                ((CustomList) itemAdapter).selectAll();
                hideMultiSelectButtons();
            }
        });
    }

    /**
     * transfers main activity to multi select mode
     */
    private void hideMultiSelectButtons() {
        Button selectAllButton = findViewById(R.id.select_all_button);
        TextView totalTextView = findViewById(R.id.total);
        Button addButton = findViewById(R.id.add_item);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button deleteButton = findViewById(R.id.delete_button_multiple);

        selectAllButton.setVisibility(View.GONE);
        totalTextView.setVisibility(View.GONE);
        addButton.setVisibility(View.GONE);

        cancelButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * deletes all items selected with a checkbox
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                ArrayList<Item> selectedItems = ((CustomList) itemAdapter).getSelectedItems();

                // Implement logic to remove selected items from your dataList
                dataList.removeAll(selectedItems);

                // Remove Items from FireStore collection
                if (user_collection != null) {
                    for (Item i : selectedItems) {
                        user_collection.document(Integer.toString(i.hashCode())).delete();
                    }
                }
                // Notify the adapter that the data has changed
                itemAdapter.notifyDataSetChanged();
                ((CustomList) itemAdapter).clearSelectedItems();

                // backup in case of sort
                originalOrderDataList.clear();
                originalOrderDataList.addAll(dataList);

                // Update the total TextView
                totalTextView.setText(String.format("$%.2f", sumItems(dataList)));

                // Clear the selected items list
                // Exit multi-select mode and show the original buttons
                ((CustomList) itemAdapter).setMultiSelectMode(false);
                showMultiSelectButtons();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            /**
             * exits multiselect mode back to main screen
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                ((CustomList) itemAdapter).setMultiSelectMode(false); // Exit multi-select mode
                showMultiSelectButtons(); // Show the original buttons
            }
        });
    }

    /**
     * transfers view elements from multiselect mode to main screen mode
     */
    private void showMultiSelectButtons() {
        Button selectAllButton = findViewById(R.id.select_all_button);
        TextView totalTextView = findViewById(R.id.total);
        Button addButton = findViewById(R.id.add_item);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button deleteButton = findViewById(R.id.delete_button_multiple);

        selectAllButton.setVisibility(View.VISIBLE);
        totalTextView.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
    }
    private Photo createPlaceholderPhoto() {
        // Create a placeholder image
        Bitmap placeholderBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        placeholderBitmap.eraseColor(getResources().getColor(android.R.color.darker_gray));

        // Create and return a Photo instance with a placeholder URI and bitmap
        return new Photo(null, placeholderBitmap);    }

    /**
     * Part of the Listener interface.
     * When the user selects OK from the filter dialogue, MainActivity starts handles the update to listview.
     */
    @Override
    public void onFilterOkPressed(String[] keywords, String[] makes, ArrayList<Tag> tags, Date startDate, Date endDate) {
        // if sort not closed
        if (sortTypeLayout.getVisibility() == View.VISIBLE) {
            cleanupSort();
        }
        // if filter already pressed redo filter
        if (filterTypeLayout.getVisibility() == View.VISIBLE) {
            cleanupFilter();
        }

        // Create a list of predicates based on conditions
        List<Predicate<Item>> conditions = new ArrayList<>();

        // desc keywords
        if (keywords.length != 0) {
            conditions.add(item -> Arrays.stream(keywords).anyMatch(item.getDescription()::contains));
        }

        // makes
        if (makes.length != 0) {
            conditions.add(item -> Arrays.stream(makes).anyMatch(item.getMake()::contains));
        }

        // tags
        if (tags.size() != 0) {
            conditions.add(item -> tagManager.filterByTags(new HashSet<>(tags)).contains(item));
        }

        // date
        if (startDate != null) {
            conditions.add(item -> isItemWithinDateRange(item, startDate, endDate));
        }

        // combine the predicates
        Predicate<Item> combinedCondition = conditions.stream().reduce(Predicate::and).orElse(item -> true);

        // filter the list based on the combined condition
        List<Item> filteredList = dataList.stream()
                .filter(combinedCondition)
                .collect(Collectors.toList());

        // update dataList with the filtered results
        dataList.clear();
        dataList.addAll(filteredList);

        // if a filter is present
        if (keywords.length != 0 || makes.length != 0 || tags.size() != 0 || startDate != null) {
            filterTypeLayout.setVisibility(View.VISIBLE);
        }
        // if nothing matches filter
        if (dataList.isEmpty()) {
            Toast.makeText(this, "No items match your filter.", Toast.LENGTH_SHORT).show();
        }

        itemAdapter.notifyDataSetChanged();
    }

    /**
     * Handles sorting of the data based on the selected sort type and order.
     * @param sortType the type of sorting (e.g. "date", "desc", "make", "value", or "NONE")
     * @param isAscending a boolean saying whether to sort in ascending order (true) or descending order (false)
     */
    @Override
    public void onSortOKPressed(String sortType, Boolean isAscending) {

        // if filter not closed
        if (filterTypeLayout.getVisibility() == View.VISIBLE) {
            cleanupFilter();
        }
        sortTypeLayout.setVisibility(View.VISIBLE);
        switch (sortType) {
            case "date":
                if (isAscending) {
//                    Toast.makeText(this, "SORT BY date ASC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item::getDate));
                } else {
//                    Toast.makeText(this, "SORT BY date DESC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item::getDate).reversed());
                }
                sortTypeTextView.setText("Date");
                itemAdapter.notifyDataSetChanged();
                break;
            case "desc":
                if (isAscending) {
//                    Toast.makeText(this, "SORT BY desc ASC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item::getDescription));
                } else {
//                    Toast.makeText(this, "SORT BY desc DESC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item::getDescription).reversed());
                }
                sortTypeTextView.setText("Description");
                itemAdapter.notifyDataSetChanged();
                break;
            case "make":
                if (isAscending) {
//                    Toast.makeText(this, "SORT BY make ASC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item::getMake));
                } else {
//                    Toast.makeText(this, "SORT BY make DESC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item::getMake).reversed());
                }
                sortTypeTextView.setText("Make");
                itemAdapter.notifyDataSetChanged();
                break;
            case "value":
                if (isAscending) {
//                    Toast.makeText(this, "SORT BY value ASC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item::getEstimatedValue));
                } else {
//                    Toast.makeText(this, "SORT BY value DESC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item::getEstimatedValue).reversed());
                }
                sortTypeTextView.setText("Estimated Value");
                itemAdapter.notifyDataSetChanged();
                break;
            case "tags":
                if (isAscending) {
//                    Toast.makeText(this, "SORT BY value ASC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item -> tagManager.printItemTags(Item, false)));
                } else {
//                    Toast.makeText(this, "SORT BY value DESC", Toast.LENGTH_SHORT).show();
                    dataList.sort(Comparator.comparing(Item -> tagManager.printItemTags((com.example.belongingsbuddy.Item) Item, false)).reversed());
                }
                sortTypeTextView.setText("Tags");
                itemAdapter.notifyDataSetChanged();
                break;
            case "NONE":
//                Toast.makeText(this, "No selection", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Part of the Listener interface.
     * When the user selects "Input manually" from the Scar or Manual prompt, MainActivity starts an
     * AddItemActivity
     */
    @Override
    public void inputManually(){
        Intent i = new Intent(MainActivity.this, AddItemActivity.class);
        i.putExtra("Manager", tagManager);
        startActivityForResult(i, REQUEST_CODE_ADD);
    }


    /**
     * Creates a Scanner Activity and gives a result
     */
    @Override
    public void inputBarcode(){
        Intent i = new Intent(MainActivity.this, ScannerActivity.class);
        startActivityForResult(i, REQUEST_CODE_BARCODE);
    }


    /**
     * When an Activity with a request code is completed, MainActivity deals with the result depending on
     * the requestCode and the resultCode
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        lastResult = resultCode;
        switch (requestCode){
            case REQUEST_CODE_ADD:
                // There are 2 possible outcomes when the REQUEST_CODE_ADD requestCode is received |
                // Only RESULT_OK requires further work from ActivityMain
                if(resultCode == Activity.RESULT_OK) {
                    // RESULT_OK indicates that all the required fields were correctly filled out
                    // and the user clicked "confirm"
                    // get all the data given by user
                    String name = data.getStringExtra("name");
                    String description = data.getStringExtra("description");
                    String make = data.getStringExtra("make");
                    String model = data.getStringExtra("model");
                    Float value = data.getFloatExtra("value", 0);
                    String comment = data.getStringExtra("comment");
                    String serialNumber = data.getStringExtra("serial number");
                    int day = data.getIntExtra("day", 0);
                    int month = data.getIntExtra("month", 0);
                    int year = data.getIntExtra("year", 0);
                    Integer quantity = data.getIntExtra("quantity", 1);
                    List<String> photoURLs = new ArrayList<>();
                    int listSize = data.getIntExtra("url list size", 0);
                    String URL;
                    for (int i = 0; i < listSize; i++) {
                        URL = data.getStringExtra("photoURL"+i);
                        Log.d("PHOTO URL RECEIVED "+i, URL);
                        photoURLs.add(URL);
                    }

                    ArrayList<Tag> selectedTags = (ArrayList<Tag>) data.getBundleExtra("BUNDLE").getSerializable("tagList");
                    // construct a Date object
                    Date date = new Date(day, month, year);
                    // construct an Item object (call constructors depending on whether or not a serial number was given)
                    Item item;
                    if (serialNumber == null) {
                        item = new Item(name, date, description, make, model, value, comment);
                        item.setQuantity(quantity);
                        item.setPhotoURLs(photoURLs);
                        dataList.add(item);
                    } else {
                        item = new Item(name, date, description, make, model, value, comment, serialNumber);
                        item.setQuantity(quantity);
                        item.setPhotoURLs(photoURLs);
                        dataList.add(item);
                    }
                    // add Item to FireStore database
                    tagManager.setItemTags(item, selectedTags);
                    Log.d("tag", item.getEpoch());

                    if (user_collection != null) {
                        item.addToDatabase(user_collection, tagManager);
                    }

//                    ArrayList tagSet = new ArrayList();
//                    tagSet.add(new Tag("tag"));
//                    tagManager.AddItem(item);
                    itemAdapter.notifyDataSetChanged();
                    // update datalist backup
                    originalOrderDataList.clear();
                    originalOrderDataList.addAll(dataList);
                    // update total
                    totalTextView.setText(String.format("$%.2f", sumItems(dataList)));
                }
                break;

            case REQUEST_CODE_VIEW:
                // there are 3 possible outcomes when a REQUEST_CODE_VIEW requestCode is received |
                // only two of them require further work from MainActivity
                if (resultCode == ItemViewActivity.REQUEST_CODE_EDIT) {
                    // CASE 1: User clicked the "Edit" button from the ItemViewActivity screen
                    // start an EditItemActivity for the Item originally clicked
                    Intent startIntent = new Intent(MainActivity.this, EditItemActivity.class);
                    Bundle itemInfo = data.getBundleExtra("item");
                    startIntent.putExtra("item", itemInfo);
                    startActivityForResult(startIntent, REQUEST_CODE_EDIT);
                } else if (resultCode == ItemViewActivity.REQUEST_CODE_DELETE) {
                    // CASE 2: User clicked the "Delete" button from the ItemViewActivity screen
                    // delete the Item from the dataList and make other necessary changes
                    int position = data.getIntExtra("position", 0);
                    float value = dataList.get(position).getEstimatedValue();
                    Item i = dataList.get(position);
                    dataList.remove(i);
                    // remove Item from FireStore collection
                    if (user_collection != null) {
                        user_collection.document(Integer.toString(i.hashCode())).delete();
                    }
                    itemAdapter.notifyDataSetChanged();
                    // update datalist backup
                    originalOrderDataList.clear();
                    originalOrderDataList.addAll(dataList);
                    // update total
                    totalTextView.setText(String.format("$%.2f", sumItems(dataList)));
                }
            case REQUEST_CODE_EDIT:
                // there are 2 possible outcomes when the REQUEST_CODE_EDIT requestCode is received |
                // only one of them requires further work from ActivityMain
                if (resultCode == Activity.RESULT_OK){
                    Bundle info = data.getExtras();
                    // get correct Item to edit
                    Integer index = info.getInt("index");
                    // update info about the edited Item
                    Item item = dataList.get(index);
                    String oldName = item.getName();
                    // get old value
                    float oldValue = item.getEstimatedValue();
                    // update info about the edited Item
                    item.setName(info.getString("name"));
                    item.getDate().setDay(info.getInt("day"));
                    item.getDate().setMonth(info.getInt("month"));
                    item.getDate().setYear(info.getInt("year"));
                    item.setDescription(info.getString("description"));
                    item.setMake(info.getString("make"));
                    item.setModel(info.getString("model"));
                    item.setEstimatedValue(info.getFloat("value"));
                    item.setSerialNumber(info.getString("serial number"));
                    item.setComment(info.getString("comment"));
                    item.setQuantity(info.getInt("quantity"));
                    List<String> photoURLs = new ArrayList<>();
                    int listSize = data.getIntExtra("url list size", 0);
                    String URL;
                    for (int i = 0; i < listSize; i++) {
                        URL = data.getStringExtra("photoURL"+i);
                        Log.d("PHOTO URL RECEIVED "+i, URL);
                        photoURLs.add(URL);
                    }
                    ArrayList<Tag> selectedTags = (ArrayList<Tag>) data.getBundleExtra("BUNDLE").getSerializable("tagList");
                    tagManager.setItemTags(item, selectedTags);
                    item.setPhotoURLs(photoURLs);
                    itemAdapter.notifyDataSetChanged();
                    // update datalist backup
                    originalOrderDataList.clear();
                    originalOrderDataList.addAll(dataList);
                    // update item in FireStore
                    if (user_collection != null) {
                        item.updateInDatabase(user_collection, tagManager);
                    }
                    // update total
                    totalTextView.setText(String.format("$%.2f", sumItems(dataList)));
                }
                break;
            case REQUEST_CODE_BARCODE:
                String productInfo = null;
                String serialnum = null;
                if (data != null) {
                    productInfo = data.getStringExtra("result");
                    serialnum = data.getStringExtra("serial");
                }
                if (productInfo != null && serialnum != null) {
                    //Add the rest of the item manually in case of incomplete data
                    Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                    intent.putExtra("productInfo", productInfo);
                    intent.putExtra("serial", serialnum);
                    startActivityForResult(intent, REQUEST_CODE_ADD);
                }
        }
    }

    private static boolean isItemWithinDateRange(Item item, Date startDate, Date endDate) {
        Date itemDate = item.getDate();
        // Compare the item date with the start and end dates
        return startDate.compareTo(itemDate) <= 0 && endDate.compareTo(itemDate) >= 0;
    }

    /**
     * Calculates the sum of estimated values of items in the given ArrayList.
     * Multiplies each Item's value by the quantity of that Item
     * @param dataList the ArrayList of Items
     * @return the sum of estimated values of items
     * @throws NullPointerException if ArrayList is null
     */
    public float sumItems(@NonNull ArrayList<Item> dataList) {
            float sum = 0f;
            for (Item item: dataList) {
                int q = item.getQuantity();
                sum += (item.getEstimatedValue() * q);
            }
            return sum;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public void tagListen(ArrayList<Tag> tagList) {
        tagManager.setTags(new HashSet<>(tagList));
        Log.d("taglist2", tagList.toString());
        tagManager.updateDatabaseTags(user_collection);
    }

    public void cleanupSort() {
        // hide selected filters
        sortTypeLayout.setVisibility(View.GONE);
        // rollback to original ordering
        dataList.clear();
        dataList.addAll(originalOrderDataList);
        itemAdapter.notifyDataSetChanged();
    }
    public void cleanupFilter() {
        // hide selected filters
        filterTypeLayout.setVisibility(View.GONE);
        // rollback to original ordering
        dataList.clear();
        dataList.addAll(originalOrderDataList);
        itemAdapter.notifyDataSetChanged();
    }
}
