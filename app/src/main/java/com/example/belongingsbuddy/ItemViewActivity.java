package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Activity for viewing the details of an Item from the user's dataList
 */
public class ItemViewActivity extends AppCompatActivity {
    public final static int REQUEST_CODE_BACK = 1;
    public final static int REQUEST_CODE_EDIT = 2;
    public final static int REQUEST_CODE_DELETE = 3;
    private TextView name;
    private TextView date;
    private TextView description;
    private TextView make;
    private TextView model;
    private TextView value;
    private TextView serialNum;
    private TextView comment;

    private TextView tags;

    /**
     * Display the activity_item_view View and fill the view with information about the Item
     * (this information is given to ItemViewActivity as extras in the calling Intent).
     *  If the user clicks the "Back" button, then give REQUEST_CODE_BACK to the returnIntent
     *  If the user clicks the "Edit" button, then give REQUEST_CODE_EDIT to the returnIntent
     *  If the user clicks the "Delete" button, then give REQUEST_CODE_DELETE to the the returnIntent
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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
        tags = this.findViewById(R.id.view_tags);

        // set up the TextViews with the information from the Item
        name.setText(getIntent().getStringExtra("name"));
        date.setText(getIntent().getStringExtra("date"));
        description.setText(getIntent().getStringExtra("description"));
        make.setText(getIntent().getStringExtra("make"));
        model.setText(getIntent().getStringExtra("model"));
        Float val = getIntent().getFloatExtra("value",0);
        value.setText(String.format("%.2f", val));
        serialNum.setText(getIntent().getStringExtra("serialNum"));
        comment.setText(getIntent().getStringExtra("comment"));
        tags.setText(getIntent().getStringExtra("tags"));




        // BACK button implementation
        final Button backButton = findViewById(R.id.view_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(REQUEST_CODE_BACK, returnIntent);
                finish();
            }
        });

        // EDIT button implementation
        final Button editButton = findViewById(R.id.view_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("item", extras);
                setResult(REQUEST_CODE_EDIT, returnIntent);
                finish();
            }
        });

        // DELETE button implementation
        final Button deleteButton = findViewById(R.id.view_belete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = getIntent().getIntExtra("index", 0);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("position", i);
                setResult(REQUEST_CODE_DELETE, returnIntent);
                finish();
            }
        });
    }


}