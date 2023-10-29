package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Item> dataList;
    private ListView itemListView;
    private ArrayAdapter<Item> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First: set up dataList, itemListView, and itemAdapter
        dataList = new ArrayList<Item>();
        itemListView = findViewById(R.id.item_list);
        //itemAdapter = new CustomList(this, dataList);
        //itemListView.setAdapter(itemAdapter);
    }
}