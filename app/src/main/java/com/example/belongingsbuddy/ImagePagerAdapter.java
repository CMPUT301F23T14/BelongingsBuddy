package com.example.belongingsbuddy;//package com.example.belongingsbuddy;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.util.ArrayList;
//
//public class ImageAdapter extends ArrayAdapter<Photo> {
//
//    private Context context;
//    private ArrayList<Photo> images;
//
//    public ImageAdapter(Context context, ArrayList<Photo> images) {
//        super(context, 0, images);
//        this.context = context;
//        this.images = images;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false);
//        }
//
//        ImageView imageView = convertView.findViewById(R.id.image_view);
//        Photo photo = getItem(position);
//
//        if (photo != null) {
//            imageView.setImageBitmap(photo.getBitmap());
//        }
//
//        return convertView;
//    }
//}

// ImagePagerAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private List<String> imageUrls;
    private LayoutInflater inflater;

    public ImagePagerAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.image_view_pager);
        Picasso.get().load(imageUrls.get(position)).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
