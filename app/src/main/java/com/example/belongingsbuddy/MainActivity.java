package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private ArrayList<Item> dataList;
    private ListView itemListView;
    private ArrayAdapter<Item> itemAdapter;
    private TextView total;
    private FirebaseFirestore db;
    private String username;

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
                "Hermann Miller", "Chair 9000", 200,  "I like this chair");
        Item testItem2 = new Item("Table", new Date(), "A table",
                "Ikea", "Table 9000", 400,  "I like this table");
        Item testItem3 = new Item("Lamp", new Date(), "A lamp",
                "Amazon", "Lamp 9000", 50,  "I like this lamp");
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
                // get the name of item
                String value = itemAdapter.getItem(position).toString();
                // start up new activity
                Intent myIntent = new Intent(MainActivity.this, ItemViewActivity.class);
                myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
            }
        });

        // ADD ITEM implementation:

        final Button addButton = findViewById(R.id.add_item);
        addButton.setOnClickListener(v -> {
            new ScanOrManual().show(getSupportFragmentManager(), "Add Item:");
            itemAdapter.notifyDataSetChanged();
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

        //Create Fragment by clicking tag
        Button openTagsButton = findViewById(R.id.tag_button);
        openTagsButton.setOnClickListener(v -> {
            TagActivity TagFragment = new TagActivity();
            TagFragment.show(getSupportFragmentManager(), "dialog");
        });
    }
}