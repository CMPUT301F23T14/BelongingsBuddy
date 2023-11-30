package com.example.belongingsbuddy;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.processing.SurfaceProcessorNode;
import androidx.annotation.Nullable;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 *  Activity for editing an Item from the user's dataList.
 *  This class gets all the necessary input from the user and then returns that
 *  data to the calling activity (MainActivity)
 */
public class EditItemActivity extends AppCompatActivity {
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
    private Float new_val;
    private EditText serialNum_text;
    private String serialNum;
    private EditText comment_text;
    private String comment;
    private ArrayList<String> photoURLs = new ArrayList<>();

    StorageReference storageReference;

    private Integer day = null;
    private Integer month = null;
    private Integer year = null;

    // photo stuff !
    private static final int PICK_IMAGES_REQUEST_CODE = 1;
    private ArrayList<Photo> selectedImages = new ArrayList<>();
    private ArrayList<Photo> savedImages = new ArrayList<>();
    private ArrayList<Uri> imageURIs = new ArrayList<Uri>();

    /**
     * Display the activity_edit_item View and wait for user input.
     * If the user clicks the "Cancel" button, end the activity.
     * If the user clicks "Confirm," this class verifies that all the required fields were filled out by
     * the user, and then returns that data to the calling activity (MainActivity)
     * NOTE: if invalid input is given and the user clicks the "Confirm" button, then this class will
     * notify the user about any issues and wait for new user input
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Bundle itemInfo = getIntent().getBundleExtra("item");
        // Initialize storage reference
        storageReference = FirebaseStorage.getInstance().getReference();
        // setup the view with information about the Item being edited
        // name:
        name_text = this.findViewById(R.id.edit_name);
        name_text.setText(itemInfo.getString("name"));
        // date:
        date_text = this.findViewById(R.id.add_date);
        date_text.setText(itemInfo.getString("date"));
        // description:
        description_text = this.findViewById(R.id.edit_description);
        description_text.setText(itemInfo.getString("description"));
        // make:
        make_text = this.findViewById(R.id.edit_make);
        make_text.setText(itemInfo.getString("make"));

        // model:
        model_text = this.findViewById(R.id.edit_model);
        model_text.setText(itemInfo.getString("model"));
        // value:
        value_text = this.findViewById(R.id.edit_value);
        Float value = itemInfo.getFloat("value");
        value_text.setText(value.toString());
        // serial number:
        serialNum_text = this.findViewById(R.id.edit_serial_number);
        Integer serial = itemInfo.getInt("serialNum");
        if (serial != 0) {
            serialNum_text.setText(serial.toString());
        }
        //comment
        comment_text = this.findViewById(R.id.edit_comment);
        comment_text.setText(itemInfo.getString("comment"));

        if (itemInfo.containsKey("selectedImages")) {
            selectedImages = itemInfo.getParcelableArrayList("selectedImages");

            // Store the selected images separately in the storedImages array
            savedImages.addAll(selectedImages);
        }

        // SET DATE implementation
        Button setDate = findViewById(R.id.edit_pick_date_button);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "Date");
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
                // if they are not all filled out, alert the user and set valid to false
                for (int i = 0; i < required.size(); i++) {
                    if (required.get(i).getText().toString().trim().length() == 0) {
                        if (valid) {
                            Toast.makeText(EditItemActivity.this, "Missing required fields", Toast.LENGTH_SHORT).show();
                        }
                        prompts[i].setBackgroundColor(getResources().getColor(R.color.light_red));
                        valid = false;
                    }
                }

                //assert a Date has been provided
                // if it has not been provided, alert the user and set valid to false
                if (date_text.getText().toString().equals("yyyy-mm-dd")) {
                    TextView prompt = prompts[5];
                    if (valid) {
                        Toast.makeText(EditItemActivity.this, "Missing required fields", Toast.LENGTH_SHORT).show();
                    }
                    prompt.setBackgroundColor(getResources().getColor(R.color.light_red));
                    valid = false;
                } else {
                    // a date has been provided
                    date = new Date(date_text.getText().toString());
                }
                // all required fields have been filled out
                if (valid) {
                    // get the updated item info
                    name = name_text.getText().toString();
                    description = description_text.getText().toString();
                    make = make_text.getText().toString();
                    model = model_text.getText().toString();
                    new_val = Float.parseFloat(value_text.getText().toString());
                    // comment is optional
                    if (TextUtils.isEmpty(comment_text.getText().toString())) {
                        comment = "NA";
                    } else {
                        comment = comment_text.getText().toString();
                    }
                    // serial number is optional
                    if (TextUtils.isEmpty(serialNum_text.getText().toString())) {
                        // use the constructor without a serial number
                        serialNum = null;
                    } else {
                        serialNum = serialNum_text.getText().toString();
                    }
                    // create returnIntent and pass needed data as extras
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
                    returnIntent.putExtra("selectedImages", selectedImages);
                    returnIntent.putExtra("url list size", photoURLs.size());
                    for (int i = 0; i < photoURLs.size(); i++) {
                        returnIntent.putExtra("photoURL"+i, photoURLs.get(i));
                    }

                    //returnIntent.putParcelableArrayListExtra("selectedImages", selectedImages);
                    setResult(Activity.RESULT_OK, returnIntent);

                    // TODO: ACTUALLY PUT PHOTO CODE HERE OOPS
                    // Defining the child of storageReference
                    StorageReference ref
                            = storageReference
                            .child(
                                    "images/"
                                            + UUID.randomUUID().toString());
                    for (int i = 0; i < imageURIs.size(); i++) {
                        // upload file 2 cloud storage :3
                        ref.putFile(imageURIs.get(i));
                    }

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

        // Photo button implementation;
        Button editPhotoButton = findViewById(R.id.edit_photo);
        editPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPhotoDialog();
            }

            private void showAddPhotoDialog() {
                // Create a custom layout for the dialog
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.add_photo, null);

                // Find the buttons in the custom layout
                Button takePhotoButton = dialogView.findViewById(R.id.take_photo_button);
                Button chooseFromGalleryButton = dialogView.findViewById(R.id.choose_from_gallery_button);

                // Set click listeners for the buttons

                chooseFromGalleryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setType("image/*");

                        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST_CODE);

                    }

                });

                // Build the custom dialog
                //AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                AlertDialog.Builder builder = new AlertDialog.Builder(EditItemActivity.this);
                builder.setView(dialogView);

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }


        });


        Button showImagesButton = findViewById(R.id.show_images_button);
        showImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectedImages();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
//                Bundle itemInfo = getIntent().getBundleExtra("item");
//                //Item currentItem = itemInfo.getParcelable("item");
//                Item currentItem = new Item(
//                        itemInfo.getString("name"),
//                        new Date(itemInfo.getString("date")),
//                        itemInfo.getString("description"),
//                        itemInfo.getString("make"),
//                        itemInfo.getString("model"),
//                        itemInfo.getFloat("value"),
//                        itemInfo.getString("comment"),
//                        itemInfo.getInt("serialNum")
//                );


                // Check if the data contains multiple images
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        imageURIs.add(uri);


                        // Load the image from the Uri
                        Bitmap imageBitmap = loadImageFromUri(uri);
                        

                        // Check for null before adding to the list
                        if (imageBitmap != null && uri != null) {
                            //currentItem.addPhoto(new Photo(uri, imageBitmap));
                            selectedImages.add(new Photo(uri, imageBitmap));
                        }

                        // Defining the child of storageReference
                        StorageReference ref
                                = storageReference
                                .child(
                                        "images/"
                                                + UUID.randomUUID().toString());
                        String URL = ref.getDownloadUrl().toString();

                        photoURLs.add(URL);

                    }
                } else {
                    // Single image selected
                    Uri uri = data.getData();
                    imageURIs.add(uri);

                    // Load the image from the Uri
                    Bitmap imageBitmap = loadImageFromUri(uri);
                    //selectedImages.add(new Photo(uri, imageBitmap));

                    // Check for null before adding to the list
                    if (imageBitmap != null && uri != null) {
                        selectedImages.add(new Photo(uri, imageBitmap));
                        //currentItem.addPhoto(new Photo(uri, imageBitmap));
                    }

                    // Defining the child of storageReference
                    StorageReference ref
                            = storageReference
                            .child(
                                    "images/"
                                            + UUID.randomUUID().toString());

                    // upload file 2 cloud storage :3
                    ref.putFile(uri);
                    String URL = ref.getDownloadUrl().toString();

                    photoURLs.add(URL);
                }



                //currentItem.setPhotos(currentItem.getPhotos());

                // Update the "photos" property of the item in the bundle
//                itemInfo.putParcelableArrayList("photos", currentItem.getPhotos());
//                getIntent().putExtra("item", itemInfo);
//                int numberOfPhotos = currentItem.getPhotos().size();
//                Toast.makeText(this, "Number of photos added: " + numberOfPhotos, Toast.LENGTH_SHORT).show();


                //int numberOfPhotos = selectedImages.size();
                //System.out.println("Number of photos in the array: " + numberOfPhotos);
            }
        }

}

    private Bitmap loadImageFromUri(Uri uri) {
        try {
            // Use ContentResolver to open an input stream from the URI
            InputStream inputStream = getContentResolver().openInputStream(uri);

            // Decode the input stream into a Bitmap
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ImageLoading", "Error loading image from Uri: " + e.getMessage());
            return null;
        }
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

    public  void getDate(Integer day, Integer month, Integer year){
        this.day = day;
        this.month = month;
        this.year = year;
        Toast.makeText(this, year.toString(), Toast.LENGTH_SHORT).show();

    }
    private void showSelectedImages() {
        // Retrieve the Item object from the bundle
        Bundle itemInfo = getIntent().getBundleExtra("item");
        //Item currentItem = itemInfo.getParcelable("item");

//        Item currentItem = new Item(
//                itemInfo.getString("name"),
//                new Date(itemInfo.getString("date")),
//                itemInfo.getString("description"),
//                itemInfo.getString("make"),
//                itemInfo.getString("model"),
//                itemInfo.getFloat("value"),
//                itemInfo.getString("comment"),
//                itemInfo.getInt("serialNum")
//        );
//
//        // Get the list of photos associated with the item
//        ArrayList<Photo> itemPhotos = currentItem.getPhotos();
//        Log.d("ImageDebug", "Number of photos: " + itemPhotos.size());
        // Create an AlertDialog to display the list of selected images

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selected Images");

        // Create a layout inflater to inflate a custom layout for the dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_show_images, null);

        // Find the ListView in the custom layout
        ListView listView = dialogView.findViewById(R.id.image_list_view);

        // Create an adapter to display the list of images
        ImageAdapter imageAdapter = new ImageAdapter(this,selectedImages);
       // ImageAdapter imageAdapter = new ImageAdapter(this, itemPhotos);
        listView.setAdapter(imageAdapter);

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
    }
}