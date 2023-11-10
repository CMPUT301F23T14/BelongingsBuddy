package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements Listener{
    private ArrayList<Item> dataList;
    private ArrayList<Item> originalOrderDataList;
    private ListView itemListView;
    private ArrayAdapter<Item> itemAdapter;
    private TextView totalTextView;
    private FirebaseFirestore db;
    private String username;
    private LinearLayout sortTypeLayout;
    private TextView sortTypeTextView;

    private TagManager tagManager = new TagManager();

    public final static int REQUEST_CODE_ADD = 1;
    public final static int REQUEST_CODE_VIEW = 2;
    public final static int REQUEST_CODE_EDIT = 3;
    public final static int REQUEST_CODE_BARCODE = 10;
    public static Integer lastResult = 1000;


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
            auth.signInWithEmailAndPassword(getString(R.string.test_email), getString(R.string.test_password));
        }
        username_button.setText(username);

        // create collection for current user (I setup firestore rules to only allow users to edit their own collections)
        db = FirebaseFirestore.getInstance();
        String uID = "";
        if (auth.getCurrentUser() != null) {
            uID = auth.getCurrentUser().getUid();
            CollectionReference user_collection = db.collection(uID); // collection name MUST be the FirestoreAuth uID
        }

        // First: set up dataList, itemListView, and itemAdapter
        dataList = new ArrayList<Item>();

        Item testItem1 = new Item("Chair", new Date(), "A chair",
                "Hermann Miller", "Chair 9000", (float) 200, "I like this chair");
        Item testItem2 = new Item("Table", new Date(), "A table",
                "Ikea", "Table 9000", (float) 400, "I like this table");
        Item testItem3 = new Item("Lamp", new Date(), "A lamp",
                "Amazon", "Lamp 9000", (float) 50, "I like this lamp");
        itemListView = findViewById(R.id.item_list);
        dataList.add(testItem1);
        dataList.add(testItem2);
        dataList.add(testItem3);

        // setup dataList copy
        // since copy is in onCreate, user can forget to clear prev sort and it will rollback properly
        // NOTE: when add method is complete, it will need to update this list in some onOkPressed method
        // otherwise it will seemingly "delete" any user added entries

        originalOrderDataList = new ArrayList<Item>();
        originalOrderDataList.addAll(dataList);
        originalOrderDataList.addAll(dataList);

        // set up itemAdapter and itemListView
        itemAdapter = new CustomList(this, dataList);
        itemListView.setAdapter(itemAdapter);

        // total
        totalTextView = findViewById(R.id.total);
        totalTextView.setText(String.format("$%.2f", sumItems(dataList)));

        // get ui objects for sort
        sortTypeLayout = findViewById(R.id.sort_type_layout);
        sortTypeTextView = findViewById(R.id.sort_type_textview);

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
                // get the Item being clicked
                Item i = itemAdapter.getItem(position);
                // setup the ItemViewActivity by creating a new Intent and passing Item data as extras
                Intent intent = new Intent(MainActivity.this, ItemViewActivity.class);
                intent.putExtra("name", i.getName());
                intent.putExtra("date", i.getDate().getString());
                intent.putExtra("description", i.getDescription());
                intent.putExtra("make", i.getMake());
                intent.putExtra("model", i.getModel());
                intent.putExtra("value", i.getEstimatedValue());
                intent.putExtra("serialNum", i.getSerialNumber());
                intent.putExtra("comment", i.getComment());
                intent.putExtra("index", position);
                startActivityForResult(intent, REQUEST_CODE_VIEW);
            }
        });

        // ADD ITEM implementation:
        final Button addButton = findViewById(R.id.add_item);
        addButton.setOnClickListener(v -> {
            ScanOrManual dialog = new ScanOrManual();
            dialog.show(getSupportFragmentManager(), "Add Item:");

        });

        // click listener sort type rollback
        final Button rollback = findViewById(R.id.sort_type_rollback);
        rollback.setOnClickListener(v -> {
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
            new SortItemsFragment().show(getSupportFragmentManager(), "Sort Item:");
        });

        // create dialog from clicking username
        username_button.setOnClickListener(new View.OnClickListener() {
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
    }

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
            @Override
            public void onClick(View v) {
                ArrayList<Item> selectedItems = ((CustomList) itemAdapter).getSelectedItems();

                // Implement logic to remove selected items from your dataList
                dataList.removeAll(selectedItems);

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
            @Override
            public void onClick(View v) {
                ((CustomList) itemAdapter).setMultiSelectMode(false); // Exit multi-select mode
                showMultiSelectButtons(); // Show the original buttons
            }
        });
    }

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

    @Override
    public void onSortOKPressed(String sortType, Boolean isAscending) {
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
                // There are 2 possible outcomes when the REQUEST_CODE_ADD requestCode is received
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
                    int serialNumber = data.getIntExtra("serial number", 0);
                    int day = data.getIntExtra("day", 0);
                    int month = data.getIntExtra("month", 0);
                    int year = data.getIntExtra("year", 0);
                  
                    ArrayList<Tag> selectedTags = (ArrayList<Tag>) data.getBundleExtra("BUNDLE").getSerializable("tagList");
                    // construct a Date object
                    // construct a Date object (call constructors depending on whether or not a serial number was given)
                    Date date = new Date(day, month, year);

                    Item item;

                    if (serialNumber == 0) {
                        item = new Item(name, date, description, make, model, value, comment);
                        dataList.add(item);
                    } else {
                        item = new Item(name, date, description, make, model, value, comment, serialNumber);
                        dataList.add(item);
                    }

                    tagManager.setItemTags(item, selectedTags);
                    itemAdapter.notifyDataSetChanged();
                    // update datalist backup
                    originalOrderDataList.clear();
                    originalOrderDataList.addAll(dataList);
                    // update total
                    totalTextView.setText(String.format("$%.2f", sumItems(dataList)));
                }
                break;

            case REQUEST_CODE_VIEW:
                // there are 3 possible outcomes when a REQUEST_CODE_VIEW requestCode is received
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
                    // delete the Item from the dataLIst and make other necessary changes
                    int position = data.getIntExtra("position", 0);
                    float value = dataList.get(position).getEstimatedValue();
                    dataList.remove(position);
                    itemAdapter.notifyDataSetChanged();
                    // update datalist backup
                    originalOrderDataList.clear();
                    originalOrderDataList.addAll(dataList);
                    // update total
                    totalTextView.setText(String.format("$%.2f", sumItems(dataList)));
                }
            case REQUEST_CODE_EDIT:
                // there are 2 possible outcomes when the REQUEST_CODE_EDIT requestCode is received
                // only one of them requires further work from ActivityMain
                if (resultCode == Activity.RESULT_OK){
                    Bundle info = data.getExtras();
                    // get correct Item to edit
                    Integer index = info.getInt("index");
                    // update info about the edited Item
                    Item item = dataList.get(index);
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
                    item.setSerialNumber(info.getInt("serial number"));
                    item.setComment(info.getString("comment"));
                    itemAdapter.notifyDataSetChanged();
                    // update datalist backup
                    originalOrderDataList.clear();
                    originalOrderDataList.addAll(dataList);
                    // update total
                    totalTextView.setText(String.format("$%.2f", sumItems(dataList)));
                }
                break;
            case REQUEST_CODE_BARCODE:
                //Add the rest of the item manually in case of incomplete data
                String productInfo = data.getStringExtra("result");
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra("productInfo", productInfo);
                startActivityForResult(intent, REQUEST_CODE_ADD);
        }
    }
public float sumItems(ArrayList<Item> dataList) {
        float sum = 0f;
        for (Item item: dataList) {
            sum += item.getEstimatedValue();
        }
        return sum;
    }
}
