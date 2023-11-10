package com.example.belongingsbuddy;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    boolean isRequestInProgress = false;
    private Handler handler = new Handler();
    private BarcodeScanner scanner = BarcodeScanning.getClient();


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

            Image mediaImage = imageProxy.getImage();
            //Check if camera has produced an image
            if (mediaImage != null) {
                detectBarcode(mediaImage, imageProxy);

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

    /**
     * Calls google ML kit and tries to find a barcode.
     * Calls lookupBarcode if found
     * @param mediaImage The image to search for barcodes
     * @param imageProxy An imageproxy with data for the mediaImage
     */
    protected void detectBarcode(Image mediaImage, ImageProxy imageProxy) {

        InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    //check if barcode is found
                    if (barcodes.size() != 0) {
                        String code = barcodes.get(0).getDisplayValue();
                        lookupBarcode(code);
                    }

                })
                .addOnFailureListener(e -> {
                        }
                );
    }

    /**
     * Calls barcode.monster api to get info from the barcode,
     * ends the activity on success
     * @param code The code to search the api for
     */
    protected void lookupBarcode(String code) {
        //Stops us from having 2 concurrent api calls
        if (!isRequestInProgress) {
            isRequestInProgress = true;
            //Use a new thread to avoid errors
            new Thread(() -> {
                //setup for api call
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://barcode.monster/api/" + code)
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    //get info from the succesful response
                    String jsonContent = response.body().string();
                    Intent intent = new Intent();
                    intent.putExtra("result", jsonContent);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    // The request is complete, so set the flag to false to allow future requests
                    isRequestInProgress = false;
                }
            }).start();
        } else {
            // A request is already in progress; you can choose to skip or queue this request, or handle it in another way.
            System.out.println("Request in progress. Skipping this request.");
        }
    }
}

