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

/**
 * ArrayAdapter class for Tag creation
 */
public class TagArrayAdapter extends ArrayAdapter<Tag> {
    private Set<Tag> tags;

    /**
     * TagArrayAdapter constructor
     * @param context activity for the adapter to be used in
     * @param resource
     * @param items the set of items objects to initialize the adapter with
     */
    public TagArrayAdapter(Context context, int resource, Set<Tag> items) {
        super(context, resource, new ArrayList<>(items));
        this.tags = items;
    }

    /**
     * Set the contents of the elements in the adapter and delete functionality for the items
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
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

    /**
     * Add a new tag to the adapter
     * @param newTag The object to add at the end of the array.
     */
    @Override
    public void add(Tag newTag) {
        if (tags.add(newTag)) {
            TagArrayAdapter.super.add(newTag);
        }
    }

    /**
     * insert a tag into a given index position in the adapter
     * @param newTag The object to insert into the array.
     * @param position The index at which the object must be inserted.
     */
    @Override
    public void insert(Tag newTag, int position) {
        if (tags.add(newTag)) {
            super.insert(newTag, position);
        }
    }

    /**
     * Remove a tag from the adapter
     * @param newTag The object to remove.
     */
    @Override
    public void remove(Tag newTag) {
        tags.remove(newTag);
        super.remove(newTag);
    }

    /**
     * Clear the adapter
     */
    @Override
    public void clear() {
        tags.clear();
        super.clear();
    }
}
