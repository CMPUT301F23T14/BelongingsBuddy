package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.processing.SurfaceProcessorNode;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Listener{
    private ArrayList<Item> dataList;
    private ListView itemListView;
    private ArrayAdapter<Item> itemAdapter;
    private TextView total;
    private FirebaseFirestore db;
    private String username;

    public final static int REQUEST_CODE_ADD = 1;
    public final static int REQUEST_CODE_VIEW = 2;
    public final static int REQUEST_CODE_EDIT = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // access username button
        final Button username_button = findViewById(R.id.username);
        // initialize username variable with placeholder value
        username = "username";

        // receive intent from Login Activity activity switch
        Intent intent = getIntent();
        // get authorization instance to get email of current user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            username = auth.getCurrentUser().getEmail().split("@")[0];
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
                "Hermann Miller", "Chair 9000", (float)200,  "I like this chair");
        Item testItem2 = new Item("Table", new Date(), "A table",
                "Ikea", "Table 9000", (float)400,  "I like this table");
        Item testItem3 = new Item("Lamp", new Date(), "A lamp",
                "Amazon", "Lamp 9000", (float)50,  "I like this lamp");
        itemListView = findViewById(R.id.item_list);
        dataList.add(testItem1);
        dataList.add(testItem2);
        dataList.add(testItem3);
        // set up itemAdapter and itemListView
        itemAdapter = new CustomList(this, dataList);
        itemListView.setAdapter(itemAdapter);

        // total
        total = findViewById(R.id.total);
        int totalInt = ((CustomList) itemAdapter).getTotal();

        total.setText(String.valueOf(totalInt));


        // click listener for items in ListView
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the Item being clicked
                Item i = itemAdapter.getItem(position);
                // setup the ItemView Activity
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

        // click listener for sort:
        final Button sortButton = findViewById(R.id.sort_button);
        sortButton.setOnClickListener(v -> {
            new SortItems().show(getSupportFragmentManager(), "Sort Item:");
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
    }

    @Override
    public void inputManually(){
        Intent i = new Intent(MainActivity.this, AddItemActivity.class);
        startActivityForResult(i, REQUEST_CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_ADD:
                if(resultCode == Activity.RESULT_OK) {
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
                    // construct a Date object
                    Date date = new Date(day, month, year);
                    if (serialNumber == 0) {
                        Item item = new Item(name, date, description, make, model, value, comment);
                        dataList.add(item);
                    } else {
                        Item item = new Item(name, date, description, make, model, value, comment, serialNumber);
                        dataList.add(item);
                    }
                    itemAdapter.notifyDataSetChanged();
                }
                break;

            case REQUEST_CODE_VIEW:
                if (resultCode == ItemViewActivity.REQUEST_CODE_EDIT) {
                    Intent startIntent = new Intent(MainActivity.this, EditItemActivity.class);
                    Bundle itemInfo = data.getBundleExtra("item");
                    startIntent.putExtra("item", itemInfo);
                    startActivityForResult(startIntent, REQUEST_CODE_EDIT);
                } else if (resultCode == ItemViewActivity.REQUEST_CODE_DELETE) {
                    int position = data.getIntExtra("position", 0);
                    dataList.remove(position);
                    itemAdapter.notifyDataSetChanged();
                }
            case REQUEST_CODE_EDIT:
                if (resultCode == Activity.RESULT_OK){
                    Bundle info = data.getExtras();
                    // get correct Item to edit
                    Integer index = info.getInt("index");
                    // update info about the edited Item
                    Item item = dataList.get(index);
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
                }
        }
    }
}
