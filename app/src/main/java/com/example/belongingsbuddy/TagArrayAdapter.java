package com.example.belongingsbuddy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class TagArrayAdapter extends ArrayAdapter<Tag> {
    private Set<Tag> tags;

    public TagArrayAdapter(Context context, int resource, Set<Tag> items) {
        super(context, resource, new ArrayList<>(items));
        this.tags = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.deletable_item, null);
        }

        TextView textViewItem = view.findViewById(R.id.textViewItem);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);

        final Tag tag = getItem(position);

        if (tag != null) {
            textViewItem.setText(tag.toString());
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle delete button click here
                    TagArrayAdapter.super.remove(tag);
                    Log.d("tag", tags.toString());
                    notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    @Override
    public void add(Tag newTag) {
        if (tags.add(newTag)) {
            TagArrayAdapter.super.add(newTag);
        }
    }

    @Override
    public void insert(Tag newTag, int position) {
        if (tags.add(newTag)) {
            super.insert(newTag, position);
        }
    }

    @Override
    public void remove(Tag newTag) {
        tags.remove(newTag);
        super.remove(newTag);
    }

    @Override
    public void clear() {
        tags.clear();
        super.clear();
    }
}
