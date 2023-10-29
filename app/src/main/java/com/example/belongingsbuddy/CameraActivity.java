package com.example.belongingsbuddy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;

/**
 * An abstract class for any activity that uses a single camera.
 * Consists of asking for permission for the camera, and creating the camera.
 * Inheriting classes must implement CameraCreationListener and set contentViewID
 * to their corresponding layout file, and said layout file must contain a
 * PreviewView with the id camera_view.
 */
public abstract class CameraActivity extends AppCompatActivity {
    Integer contentViewID;
    CameraCreationListener cameraCreationListener;
    LifecycleCameraController cameraController;

    /**
     * Creates the CameraActivity if permission is granted, otherwise asks for permission
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Must be set by subclass
        assert(contentViewID != null);
        assert(cameraCreationListener != null);

        setContentView(contentViewID);

        //check if permission is granter
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10);
        } else {
            createCamera();
        }
    }

    /**
     * Creates the camera after permission is granted, and notifies subclass
     * for failure case is camera permission is denied.
     * @param requestCode The request code passed in "{"@link #requestPermissions(
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either "{"@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or "{"@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            //Check if permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createCamera();
            }
            else {
                //notify subclass that camera creation failed
                cameraCreationListener.onCameraCreationFailure();
            }
        }
    }

    /**
     * Builds the camera. Requires a PreviewView with id as camera_view
     * within the layout. Notifies subclass on success
     */
    protected void createCamera() {
        //CameraActivity must have this view
        PreviewView previewView = findViewById(R.id.camera_view);
        //get a controller for the default back camera
        cameraController = new LifecycleCameraController(previewView.getContext());
        cameraController.setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA);
        cameraController.bindToLifecycle(this);
        //Make camera visible
        previewView.setController(cameraController);
        //notify subclass camera was created successfully
        cameraCreationListener.onCameraCreationSuccess();
    }

    public void setContentViewID(int contentViewID) {
        this.contentViewID = contentViewID;
    }

    public void setCameraCreationListener(CameraCreationListener cameraCreationListener) {
        this.cameraCreationListener = cameraCreationListener;
    }
}

/**
 * Allows for CameraActivity to notify subclasses when camera creation succeeded or failed
 */
interface CameraCreationListener {
    void onCameraCreationSuccess();
    void onCameraCreationFailure();
}