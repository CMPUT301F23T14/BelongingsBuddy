package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private ArrayList<Item> dataList;
    private ListView itemListView;
    private ArrayAdapter<Item> itemAdapter;
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
        itemListView = findViewById(R.id.item_list);
        //itemAdapter = new CustomList(this, dataList);
        //itemListView.setAdapter(itemAdapter);


        // ADD ITEM implementation:
        final Button addButton = findViewById(R.id.add_item);
        addButton.setOnClickListener(v -> {
            new ScanOrManual().show(getSupportFragmentManager(), "Add Item:");
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
}