package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.UUID;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


/**
 * Activity for adding an Item to the user's dataList.
 * This class gets all the necessary input from the user to construct a new Item and then returns that
 * data to the calling activity (MainActivity)
 */
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
    private String serialNumber;
    StorageReference storageReference;
    private EditText serialNumber_text;
    private Float value;
    private EditText value_text;
    private String comment;
    private EditText comment_text;
    private ArrayList<Tag> tags = new ArrayList<>();
    private ArrayList<Photo> photos;
    private ArrayList<String> photoURLs = new ArrayList<>();
    private static final int PICK_IMAGES_REQUEST_CODE = 1;
    private ArrayList<Photo> selectedImages = new ArrayList<>();
    private ArrayList<Photo> savedImages = new ArrayList<>();
    private ArrayList<Uri> imageURIs = new ArrayList<Uri>();
    private int currentImageIndex = 1;

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
        String serialnum = getIntent().getStringExtra("serial");
        if (productInfo != null && !productInfo.equals("failure")) {
            try {
                JSONObject productJSON = new JSONObject(productInfo);
                description_text = findViewById(R.id.add_description);
                if (productJSON.has("description")) {
                    String initialText = productJSON.getString("description");
                    description_text.setText(initialText.replace(" (from barcode.monster)", ""));
                }
            } catch (JSONException e) {
            }
        }
        else if (productInfo != null && productInfo == "failure") {
            Toast.makeText(this, "Failed to connect to barcode monster API", Toast.LENGTH_SHORT).show();
        }
        if (serialnum != null) {
            serialNumber_text = findViewById(R.id.add_serial_number);
            serialNumber_text.setText(serialnum);
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
            Intent i = getIntent();
            arg.putSerializable("tagManager", i.getSerializableExtra("Manager"));
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
                    if (serialNumber_text.getText().toString().trim().length() == 0){
                        // use the constructor without a serial number
                        serialNumber = null;
                    } else {
                        serialNumber = serialNumber_text.getText().toString();
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
                    returnIntent.putExtra("url list size", photoURLs.size());

//                    for (int i = 0; i < photoURLs.size(); i++) {
//                        returnIntent.putExtra("photoURL" + i, photoURLs.get(i));
//                    }
////
//                    //returnIntent.putParcelableArrayListExtra("selectedImages", selectedImages);
//                    setResult(Activity.RESULT_OK, returnIntent);
//
//                    // TODO: ACTUALLY PUT PHOTO CODE HERE OOPS
//                    // Defining the child of storageReference
//                    StorageReference ref
//                            = storageReference
//                            .child(
//                                    "images/"
//                                            + UUID.randomUUID().toString());
//                    for (int i = 0; i < imageURIs.size(); i++) {
//                        // upload file 2 cloud storage :3
//                        ref.putFile(imageURIs.get(i));
//                    }

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

        // Photo button implementation;
        Button editPhotoButton = findViewById(R.id.add_photo);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this);
                builder.setView(dialogView);

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }


        });


//        Button showImagesButton = findViewById(R.id.show_images_button);
//        showImagesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // showImagesButton.setOnClickListener(new View.OnClickListener() {
//                    //@Override
//                  //  public void onClick(View v) {
//                        // Load the current image URL into the ImageView using Picasso
//                        ImageView imageView = findViewById(R.id.image_view);
//                        if (photoURLs != null && !photoURLs.isEmpty()) {
//                            Picasso.get().load(photoURLs.get(currentImageIndex)).into(imageView);
//                            // Move to the next image or loop back to the first image
//                            currentImageIndex = (currentImageIndex + 1) % photoURLs.size();
//                        }
//                    }
//
//        });

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
                        // upload file 2 cloud storage :3
                        UploadTask uploadTask = ref.putFile(uri);
                        // Get the download URL
                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    // The download URL of the image
                                    Uri downloadUri = task.getResult();
                                    String downloadUrl = downloadUri.toString();
                                    Log.d("Download URL", downloadUrl);

                                    // Now you can use downloadUrl as needed, e.g., store it in a database
                                    photoURLs.add(downloadUrl);
                                } else {
                                    // Handle failures
                                    Log.e("Download URL", "Failed to get download URL");
                                }
                            }
                        });



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

    public void setTagList(ArrayList<Tag> tagList) {
        tags = tagList;
    }
}