package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    private ArrayList<Item> dataList;
    private ListView itemListView;
    private ArrayAdapter<Item> itemAdapter;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up dataList
        // test data for now
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
        // ADD Item implementation:
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
    }
}