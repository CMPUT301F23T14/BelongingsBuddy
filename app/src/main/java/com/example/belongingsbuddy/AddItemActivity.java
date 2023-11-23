package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Activity for adding an Item to the user's dataList.
 * This class gets all the necessary input from the user to construct a new Item and then returns that
 * data to the calling activity (MainActivity)
 */
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
    private ArrayList<Tag> tags = new ArrayList<>();
    private ArrayList<Photo> photos;

    /**
     * Display the activity_add_item View and wait for user input.
     * If the user clicks the "Cancel" button, end the activity.
     * If the user clicks "Confirm," this class verifies that all the required fields were filled out by
     * the user, and then returns that data to the calling activity (MainActivity)
     * NOTE: if invalid input is given and the user clicks the "Confirm" button, then this class will
     * notify the user about any issues and wait for new user input
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //Check if there is any info from barcodes
        String productInfo = getIntent().getStringExtra("productInfo");
        if (productInfo != null) {
            try {
                JSONObject productJSON = new JSONObject(productInfo);
                description_text = findViewById(R.id.add_description);

                if (productJSON.has("description")) {
                    String initialText = productJSON.getString("description");
                    description_text.setText(initialText.replace(" (from barcode.monster)", ""));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        // SET DATE implementation
        Button setDate = findViewById(R.id.add_pick_date_button);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Date");
            }
        });

        //Set Tags Implementation Dialog Frame Window
        Button openTagsButton = findViewById(R.id.add_tags_button);
        openTagsButton.setOnClickListener(v -> {
            Bundle arg = new Bundle();
            arg.putSerializable("tagManager", getIntent().getSerializableExtra("Manager"));
            arg.putSerializable("selectedTags", tags);
            TagActivity TagFragment = new TagActivity();
            TagFragment.setArguments(arg);
            TagFragment.show(getSupportFragmentManager(), "dialog");
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
                // if not, alert the user and set valid flag to false
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
                // if it has not been provided, alert the user and set value to false
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

                // If valid is true, then all the required fields have been filled out
                // get user input, and pass necessary info as extras in the returnIntent
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
                    } else {
                        serialNumber = Integer.parseInt(serialNumber_text.getText().toString());
                    }
                    // create returnIntent and pass needed data as extras
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

                    Bundle args = new Bundle();
                    args.putSerializable("tagList",tags);
                    returnIntent.putExtra("BUNDLE",args);

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

    /**
     * Sets the background color of the given TextView(s) back to their original color.
     * This is needed because they may have been previously set to red to alert the user of missing input
     * @param prompts A list of TextViews containing all the prompts that need to be "reset"
     */
    private void resetPrompts(TextView[] prompts){
        for (TextView p: prompts)
            p.setBackgroundColor(getResources().getColor(R.color.light_purple));
    }

    public void setTagList(ArrayList<Tag> tagList) {
        tags = tagList;
    }
}