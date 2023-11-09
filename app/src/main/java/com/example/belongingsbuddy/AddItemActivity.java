package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.processing.SurfaceProcessorNode;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
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
    private Item item;
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
    private Float value;
    private EditText value_text;
    private String comment;
    private EditText comment_text;
    private ArrayList<Tag> tags;
    private ArrayList<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
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
                TextView[] prompts = {findViewById(R.id.name_prompt), findViewById(R.id.description_prompt),
                        findViewById(R.id.make_prompt), findViewById(R.id.model_prompt), findViewById(R.id.value_prompt),
                        findViewById(R.id.date_prompt)};
                resetPrompts(prompts);

                // initialize an array of all the required fields
                ArrayList<EditText> required = new ArrayList<EditText>();
                required.add(name_text);
                required.add(description_text);
                required.add(make_text);
                required.add(model_text);
                required.add(value_text);

                // assert all required fields are filled out
                for (int i = 0; i < required.size(); i++){
                    if (required.get(i).getText().toString().trim().length() == 0){
                        if (valid){
                            Toast.makeText(AddItemActivity.this, "Missing required fields", Toast.LENGTH_SHORT).show();
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
                    value = Float.parseFloat(value_text.getText().toString());
                    // comment is optional
                    if (TextUtils.isEmpty(comment_text.getText().toString())){
                        comment = "NA";
                    } else{
                        comment = comment_text.getText().toString();
                    }
                    // serial number is optional
                    if (TextUtils.isEmpty(serialNumber_text.getText().toString())){
                        // use the constructor without a serial number
                        serialNumber = null;
                        //item = new Item(name, date, description, make, model, value, comment);
                    } else {
                        serialNumber = Integer.parseInt(serialNumber_text.getText().toString());
                        //item = new Item(name, date, description, make, model, serialNumber, value, comment);
                    }
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", name);
                    returnIntent.putExtra("description", description);
                    returnIntent.putExtra("make", make);
                    returnIntent.putExtra("model", model);
                    returnIntent.putExtra("value", value);
                    returnIntent.putExtra("comment", comment);
                    returnIntent.putExtra("serial number", serialNumber);
                    returnIntent.putExtra("day", date.getDay());
                    returnIntent.putExtra("month", date.getMonth());
                    returnIntent.putExtra("year", date.getYear());
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });

        // CANCEL implementation:
        Button cancel = findViewById(R.id.add_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

    }

    private void resetPrompts(TextView[] prompts){
        for (TextView p: prompts)
            p.setBackgroundColor(getResources().getColor(R.color.light_purple));
    }
}