package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.processing.SurfaceProcessorNode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity{
    private EditText name_text;
    private String name;
    private TextView date_text;
    private Date date;
    private EditText description_text;
    private String description;
    private EditText make_text;
    private String make;
    private EditText model_text;
    private String model;
    private EditText value_text;
    private  Float new_val;
    private EditText serialNum_text;
    private Integer serialNum;
    private EditText comment_text;
    private String comment;

    private Integer day = null;
    private Integer month = null;
    private Integer year = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Bundle itemInfo = getIntent().getBundleExtra("item");
        // setup the view with information about the Item being edited
        // name:
        name_text = this.findViewById(R.id.edit_name);
        name_text.setText(itemInfo.getString("name", "NA"));
        // date:
        date_text = this.findViewById(R.id.add_date);
        date_text.setText(itemInfo.getString("date"));
        // description:
        description_text = this.findViewById(R.id.edit_description);
        description_text.setText(itemInfo.getString("description"));
        // make:
        make_text = this.findViewById(R.id.edit_make);
        make_text.setText(itemInfo.getString("make"));;
        // model:
        model_text = this.findViewById(R.id.edit_model);
        model_text.setText(itemInfo.getString("model"));
        // value:
        value_text =this.findViewById(R.id.edit_value);
        Float value = itemInfo.getFloat("value");
        value_text.setText(value.toString());
        // serial number:
        serialNum_text = this.findViewById(R.id.edit_serial_number);
        Integer serial = itemInfo.getInt("serialNum");
        if (serial != 0){
            serialNum_text.setText(serial.toString());
        }
        //comment
        comment_text = this.findViewById(R.id.edit_comment);
        comment_text.setText(itemInfo.getString("comment"));

        // SET DATE implementation
        Button setDate = findViewById(R.id.edit_pick_date_button);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Date");
                //Integer day = newFragment.getDay();
                //Toast.makeText(EditItemActivity.this, day.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        // CONFIRM implementation:
        Button confirm = findViewById(R.id.edit_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            Toast.makeText(EditItemActivity.this, "Missing required fields", Toast.LENGTH_SHORT).show();
                        }
                        prompts[i].setBackgroundColor(getResources().getColor(R.color.light_red));
                        valid = false;
                    }
                }

                //assert a Date has been provided
                if (date_text.getText().toString().equals("yyyy-mm-dd")){
                    TextView prompt = prompts[5];
                    if (valid){
                        Toast.makeText(EditItemActivity.this, "Missing required fields", Toast.LENGTH_SHORT).show();
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
                    new_val = Float.parseFloat(value_text.getText().toString());
                    // comment is optional
                    if (TextUtils.isEmpty(comment_text.getText().toString())){
                        comment = "NA";
                    } else{
                        comment = comment_text.getText().toString();
                    }
                    // serial number is optional
                    if (TextUtils.isEmpty(serialNum_text.getText().toString())){
                        // use the constructor without a serial number
                        serialNum = null;
                    } else {
                        serialNum = Integer.parseInt(serialNum_text.getText().toString());
                    }
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("name", name);
                    returnIntent.putExtra("description", description);
                    returnIntent.putExtra("make", make);
                    returnIntent.putExtra("model", model);
                    returnIntent.putExtra("value", new_val);
                    returnIntent.putExtra("comment", comment);
                    returnIntent.putExtra("serial number", serialNum);
                    returnIntent.putExtra("day", date.getDay());
                    returnIntent.putExtra("month", date.getMonth());
                    returnIntent.putExtra("year", date.getYear());
                    returnIntent.putExtra("index", itemInfo.getInt("index"));
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });

        // CANCEL implementation:
        Button cancel = findViewById(R.id.edit_cancel);
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

    public  void getDate(Integer day, Integer month, Integer year){
        this.day = day;
        this.month = month;
        this.year = year;
        Toast.makeText(this, year.toString(), Toast.LENGTH_SHORT).show();

    }
}