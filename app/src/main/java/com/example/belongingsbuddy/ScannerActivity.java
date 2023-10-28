package com.example.belongingsbuddy;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * An activity used for scanning barcodes using a camera.
 */
public class ScannerActivity extends CameraActivity implements CameraCreationListener {
    /**
     * Initializes the activity when it is created
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //CameraActivity needs these fields set
        setContentViewID(R.layout.activity_scanner);
        setCameraCreationListener(this);

        super.onCreate(savedInstanceState);
    }

    /**
     * Called when camera was created successfully.
     * Processes camera frames to check for barcodes.
     */
    @OptIn(markerClass = ExperimentalGetImage.class)
    @Override
    public void onCameraCreationSuccess() {
        cameraController.setImageAnalysisAnalyzer(Executors.newSingleThreadExecutor(), imageProxy -> {
            //Calls the google ML-kit api
            BarcodeScanner scanner = BarcodeScanning.getClient();

            Image mediaImage = imageProxy.getImage();
            //Check if camera has produced an image
            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                Task<List<Barcode>> result = scanner.process(image)
                        .addOnSuccessListener(barcodes -> {
                            Log.d("", "Barcode Found");
                        })
                        .addOnFailureListener(e -> {
                        });
            }

            imageProxy.close();
        });
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

