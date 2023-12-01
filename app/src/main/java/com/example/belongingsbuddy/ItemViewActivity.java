package com.example.belongingsbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
    private TextView quantity;

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
        quantity = this.findViewById(R.id.view_quantity);
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
        Integer q = getIntent().getIntExtra("quantity", 1);
        quantity.setText(q.toString());
        tags.setText(getIntent().getStringExtra("tags"));
        int listSize = getIntent().getIntExtra("photoURLsize", 0);
        for (int i = 0; i < listSize; i++) {
            String URL = getIntent().getStringExtra("photoURL"+i);
            Log.d("URL", URL);
            photoURLs.add(URL);
        }


        Button showImagesButton = findViewById(R.id.show_images_button);
        showImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showImagesButton.setOnClickListener(new View.OnClickListener() {
                //@Override
                //  public void onClick(View v) {
                // Create an AlertDialog to display the list of selected images
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemViewActivity.this);
                builder.setTitle("Selected Images");
                // Create a layout inflater to inflate a custom layout for the dialog
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_show_images, null);
                ViewPager viewPager = dialogView.findViewById(R.id.view_pager);
                ImagePagerAdapter adapter = new ImagePagerAdapter(photoURLs);
                viewPager.setAdapter(adapter);

                // Set the custom view to the dialog
                builder.setView(dialogView);
                // Add a "Close" button to the dialog
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog
                        dialog.dismiss();
                    }
                });
                // Show the dialog
                builder.show();
                Button deleteButton = dialogView.findViewById(R.id.delete_button);


                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle delete action (implement as needed)
                        // For example, you can delete the currently displayed image
                        int currentItem = viewPager.getCurrentItem();
//                        @Override
//                        public void onClick(View v) {
//                            // 1. Identify the index of the item to delete
//                            int itemIndex = getIntent().getIntExtra("index", 0);
//
//                            // 2. Retrieve the item from dataList
//                            if (itemIndex >= 0 && itemIndex < dataList.size()) {
//                                Item itemToDelete = dataList.get(itemIndex);
//
//                                // 3. Delete the item from Firestore
//                                user_collection.document(itemToDelete.getDocumentId()).delete()
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                // 4. Delete the item from dataList
//                                                dataList.remove(itemIndex);
//                                                itemAdapter.notifyDataSetChanged();
//
//                                                // 5. Navigate back to MainActivity or perform other actions
//                                                Toast.makeText(ItemViewActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
//                                                finish();
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                // Handle the failure to delete from Firestore
//                                                Toast.makeText(ItemViewActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                            }
//                        }
//                    });
             //   }
                    }
                });



                // Load the current image URL into the ImageView using Picasso
//                ImageView imageView = dialogView.findViewById(R.id.image_view);
//                Log.d("Imageview", imageView.toString());
//                for (int i = 0; i < listSize; i++) {
//                    continue;
//                }
//                Picasso.get()
//                        .load("https://i.imgur.com/DvpvklR.png")
//                        .into(imageView, new Callback() {
//                            @Override
//                            public void onSuccess() {
//                                // Image loaded successfully
//                                Log.d("PICASSO", "Image loaded");
//                            }
//
//                            @Override
//                            public void onError(Exception e) {
//                                // Handle error
//                                e.printStackTrace();
//                            }
//                        });
//                if (photoURLs != null && !photoURLs.isEmpty()) {
//                    int currentImageIndex = 0;
//
//                    String URL = photoURLs.get(currentImageIndex);
//                    Picasso.get()
//                        .load(URL)
//                        .into(imageView, new Callback() {
//                            @Override
//                            public void onSuccess() {
//                                // Image loaded successfully
//                                Log.d("PICASSO", "Image loaded");
//                            }
//
//                            @Override
//                            public void onError(Exception e) {
//                                // Handle error
//                                Log.d("PICASSO", "Image failed to load.");
//                                e.printStackTrace();
//                            }
//                        });
//                    // Move to the next image or loop back to the first image
//                    // photoURLs.remove(currentImageIndex);
//                    currentImageIndex = (currentImageIndex + 1) % photoURLs.size();
//                }

            }

        });





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