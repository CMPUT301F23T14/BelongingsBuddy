package com.example.belongingsbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Photo> {

    private Context context;
    private ArrayList<Photo> images;

    public ImageAdapter(Context context, ArrayList<Photo> images) {
        super(context, 0, images);
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.image_view);
        Photo photo = getItem(position);

        if (photo != null) {
            imageView.setImageBitmap(photo.getBitmap());
        }

        return convertView;
    }
}

