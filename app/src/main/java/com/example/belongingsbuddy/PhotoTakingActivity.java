package com.example.belongingsbuddy;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;

import java.util.concurrent.Executors;

public class PhotoTakingActivity extends CameraActivity implements CameraCreationListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //CameraActivity needs these fields set
        setContentViewID(R.layout.activity_scanner);
        setCameraCreationListener(this);

        super.onCreate(savedInstanceState);
    }

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

}
