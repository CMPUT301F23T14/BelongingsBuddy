package com.example.belongingsbuddy;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

//public class Photo {
//    private Uri imageUri;
//    private Bitmap bitmap;
//
//    public Photo(Uri imageUri, Bitmap bitmap) {
//        this.imageUri = imageUri;
//        this.bitmap = bitmap;
//    }
//
//    public Uri getImageUri() {
//        return imageUri;
//    }
//
//    public Bitmap getBitmap() {
//        return bitmap;
//    }
//
//    // You can add more methods or properties as needed
//
//    // For example, if you want to get a thumbnail of the photo
//    public Bitmap getThumbnail() {
//        // Implement the logic to generate a thumbnail from the original bitmap
//        // For simplicity, you can just return the original bitmap in this example
//        return bitmap;
//    }
//}

public class Photo implements Parcelable {

    private Uri imageUri;
    private Bitmap bitmap;

    public Photo(Uri imageUri, Bitmap bitmap) {
        this.imageUri = imageUri;
        this.bitmap = bitmap;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    // You can add more methods or properties as needed

    // For example, if you want to get a thumbnail of the photo
    public Bitmap getThumbnail() {
        // Implement the logic to generate a thumbnail from the original bitmap
        // For simplicity, you can just return the original bitmap in this example
        return bitmap;
    }
    // Parcelable implementation
    protected Photo(Parcel in) {
        // Read data from Parcel and initialize your Photo object
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write data to Parcel for each field in your Photo class
    }
}

