package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * I started working on this before I realized I was not supposed to do it.
 * I figured to include it ¯\_(ツ)_/¯
 */
public class ItemViewActivity extends AppCompatActivity {
    TextView make;
    TextView value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_view_activity);

        make = findViewById(R.id.item_view_make_textview);
        value = findViewById(R.id.item_view_value_textview);

        // get the name of the city
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.

        // set textview
        make.setText(value);

        // this button returns us to main activity
        final Button backButton = findViewById(R.id.item_view_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ItemViewActivity.this.finish();
            }
        });
    }
}