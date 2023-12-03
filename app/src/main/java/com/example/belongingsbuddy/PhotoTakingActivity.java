package com.example.belongingsbuddy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.lifecycle.LifecycleOwner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhotoTakingActivity extends CameraActivity implements CameraCreationListener {
    private Executor executor = Executors.newSingleThreadExecutor();
    private ArrayList<Uri> takenPhotos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //CameraActivity needs these fields set
        setContentViewID(R.layout.activity_picture);
        setCameraCreationListener(this);

        super.onCreate(savedInstanceState);

        Button switchCamera = findViewById(R.id.swap_camera_button);
        Button takePhoto = findViewById(R.id.take_picture_button);
        Button backButton = findViewById(R.id.back_button_photo);
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraController.getCameraSelector() == CameraSelector.DEFAULT_BACK_CAMERA) {
                    cameraController.setCameraSelector(CameraSelector.DEFAULT_FRONT_CAMERA);
                }
                else {
                    cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);
                }
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraController.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);

                        int rotationDegrees = image.getImageInfo().getRotationDegrees();

                        byte[] rotatedBytes = rotateImage(bytes, rotationDegrees);

                        File imageFile = saveImageToFile(rotatedBytes);

                        Uri imageUri = Uri.fromFile(imageFile);
                        takenPhotos.add(imageUri);

                        Toast.makeText(PhotoTakingActivity.this, "Took Photo! taken photos:" + String.valueOf(takenPhotos.size()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        finish();
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("capturedImages", takenPhotos);

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private byte[] rotateImage(byte[] data, int rotationDegrees) {
        // Rotate the image based on the orientation information
        if (rotationDegrees == 0) {
            return data; // No rotation needed
        }

        Bitmap originalBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationDegrees);

        // Create a new rotated bitmap
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

        // Convert the rotated bitmap to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    /**
     /**
     * Called when camera was created successfully.
     */
    @OptIn(markerClass = ExperimentalGetImage.class)
    @Override
    public void onCameraCreationSuccess() {


    }

    /**
     * Camera failed to be created.
     * Goes back to previous activity.
     */
    @Override
    public void onCameraCreationFailure() {
        finish();
    }

    private File saveImageToFile(byte[] bytes) {
        File outputDir = getCacheDir();

        File outputFile = null;
        FileOutputStream fos = null;

        try {
            outputFile = File.createTempFile("image", ".jpg", outputDir);
            fos = new FileOutputStream(outputFile);
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputFile;
    }
}
