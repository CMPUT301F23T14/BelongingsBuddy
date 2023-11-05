package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.RangeValueIterator;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity{
    private String name;
    private EditText name_text;
    private Date date;
    private TextView date_text;
    private String description;
    private EditText description_text;
    private String make;
    private EditText make_text;
    private String model;
    private EditText model_text;
    private Integer serialNumber;
    private EditText serialNumber_text;
    private Integer value;
    private EditText value_text;
    private String comment;
    private EditText comment_text;
    private ArrayList<Tag> tags;
    private ArrayList<Photo> photos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        /*
        View view = getLayoutInflater().inflate(R.layout.activity_add_item, null, false);
        //View view = View.inflate(this, R.layout.activity_add_item, null);
        // get relevant EditTexts form view

        name_text = view.findViewById(R.id.add_name);
        date_text = view.findViewById(R.id.add_date);
        description_text = view.findViewById(R.id.add_description);
        make_text = view.findViewById(R.id.add_make);
        model_text = view.findViewById(R.id.add_model);
        value_text = view.findViewById(R.id.add_value);
        serialNumber_text = view.findViewById(R.id.add_serial_number);
        comment_text = view.findViewById(R.id.add_comment);
        // tags
        // photos

         */


        // SET DATE implementation
        Button setDate = findViewById(R.id.add_pick_date_button);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Date");

            }
        });

        // CONFIRM implementation:
        Button confirm = findViewById(R.id.add_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get needed EditText views
                name_text = findViewById(R.id.add_name);
                date_text = findViewById(R.id.add_date);
                description_text = findViewById(R.id.add_description);
                make_text = findViewById(R.id.add_make);
                model_text = findViewById(R.id.add_model);
                value_text = findViewById(R.id.add_value);
                serialNumber_text = findViewById(R.id.add_serial_number);
                comment_text = findViewById(R.id.add_comment);

                // reset prompts and valid flag
                boolean valid = true;
                resetPrompts();

                // initialize an array of all the required fields
                ArrayList<EditText> required = new ArrayList<EditText>();
                required.add(name_text);
                required.add(description_text);
                required.add(make_text);
                required.add(model_text);
                required.add(value_text);
                TextView[] prompts = {findViewById(R.id.name_prompt), findViewById(R.id.description_prompt),
                                        findViewById(R.id.make_prompt), findViewById(R.id.model_prompt), findViewById(R.id.value_prompt),
                                        findViewById(R.id.date_prompt)};

                // assert all required fields are filled out
                for (int i = 0; i < required.size(); i++){
                    if (required.get(i).getText().toString().trim().length() == 0){
                        if (valid){
                            Toast.makeText(AddItemActivity.this, "Missing required fields", Toast.LENGTH_SHORT).show();
                            Toast.makeText(AddItemActivity.this, prompts[i].getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                        prompts[i].setBackgroundColor(getResources().getColor(R.color.light_red));
                        valid = false;
                    }
                }

                //assert a Date has been provided
                if (date_text.getText().toString().equals("yyyy-mm-dd")){
                    TextView prompt = prompts[5];
                    if (valid){
                        Toast.makeText(AddItemActivity.this, "Missing required fields", Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddItemActivity.this, prompt.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                    prompt.setBackgroundColor(getResources().getColor(R.color.light_red));
                    valid = false;
                } else{
                    // a date has been provided
                    date = new Date(date_text.getText().toString());
                }
                // all required fields have been filled out
                if (valid){
                    name = name_text.getText().toString();
                    description = description_text.getText().toString();
                    make = make_text.getText().toString();
                    model = model_text.getText().toString();
                    value = Integer.parseInt(value_text.getText().toString());
                    serialNumber = Integer.parseInt(serialNumber_text.getText().toString());
                    comment = comment_text.getText().toString();
                    Item item = new Item(name, date, description, make, model, serialNumber, value, comment);
                    getIntent().putExtra("added item", item);
                    finish();
                }
            }
        });

        // CANCEL implementation:
        Button cancel = findViewById(R.id.add_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void resetPrompts(){
        TextView[] prompts = {findViewById(R.id.name_prompt), findViewById(R.id.description_prompt),
                findViewById(R.id.make_prompt), findViewById(R.id.model_prompt), findViewById(R.id.value_prompt)};
        for (TextView p: prompts)
            p.setBackgroundColor(getResources().getColor(R.color.light_purple));
    }
}