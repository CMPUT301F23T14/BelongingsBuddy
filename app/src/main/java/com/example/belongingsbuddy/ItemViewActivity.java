package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * I started working on this before I realized I was not supposed to do it.
 * I figured to include it ¯\_(ツ)_/¯
 */
public class ItemViewActivity extends AppCompatActivity {
    private TextView name;
    private TextView date;
    private TextView description;
    private TextView make;
    private TextView model;
    private TextView value;
    private TextView serialNum;
    private TextView comment;
    // TextView tags;
    // TextView photos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        // get the needed TextViews from the screen
        View view = LayoutInflater.from(this).inflate(R.layout.activity_item_view, null);
        name = this.findViewById(R.id.view_name);
        date = this.findViewById(R.id.view_date);
        description = this.findViewById(R.id.view_description);
        make = this.findViewById(R.id.view_make);
        model = this.findViewById(R.id.view_model);
        value = this.findViewById(R.id.view_value);
        serialNum = this.findViewById(R.id.view_serial_number);
        comment = this.findViewById(R.id.view_comment);

        // set up the TextViews with the information from the Item
        name.setText(getIntent().getStringExtra("name"));
        date.setText(getIntent().getStringExtra("date"));
        description.setText(getIntent().getStringExtra("description"));
        make.setText(getIntent().getStringExtra("make"));
        model.setText(getIntent().getStringExtra("model"));
        Float val = getIntent().getFloatExtra("value",0);
        value.setText(String.format("%.2f", val));
        Integer serial = getIntent().getIntExtra("serialNum", 0);
        if (serial != 0){
            serialNum.setText(serial.toString());
        }
        comment.setText(getIntent().getStringExtra("comment"));


        // BACK button implementation
        final Button backButton = findViewById(R.id.view_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ItemViewActivity.this.finish();
            }
        });
    }
}