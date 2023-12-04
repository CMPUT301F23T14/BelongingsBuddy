package com.example.belongingsbuddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An instance of this class represents a subclass of ArrayAdapter that also supports multiselection.
 */
public class CustomList extends ArrayAdapter<Item> {
    private final Context context;
    private ArrayList<Item> items;
    private boolean multiSelectMode;
    private ArrayList<Item> selectedItems;
    private HashMap<Integer, Boolean> itemSelection;

    /**
     * Constructor for CustomList class
     *
     * @param context context adapter uses (should be main activity)
     * @param dataList ArrayList<Item> is the dataset the listview is populated with
     *
     * @see Context
     * @see ArrayList
     */
    public CustomList(Context context, ArrayList<Item> dataList) {
        super(context, 0, dataList);
        this.items = dataList;
        this.context = context;
        this.multiSelectMode = false;
        this.selectedItems = new ArrayList<>();
        this.itemSelection = new HashMap<>();
    }
    /**
     * When multi-select mode is true, the adapter will display checkboxes for
     * each item in the list allowing the user to select multiple items.
     *
     * @param mode if true then enable multi-select mode, if false disable it.
     */
    public void setMultiSelectMode(boolean mode) {
        multiSelectMode = mode;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    /**
     * Getter for multiSelectMode
     * @return multiSelectMode of type boolean
     */
    public boolean isMultiSelectMode() {
        return multiSelectMode;
    }

    public ArrayList<Item> getSelectedItems() {
        return selectedItems;
    }
    /**
     * Returns a view that displays the data at the specified position in the data set.
     * The checkbox is hidden by default and becomes visible when multiSelectMode is true.
     * The method also handles checkbox selection events, updating the selectedItems list accordingly.
     *
     * @param position The position of the item within the adapter's data set.
     * @param convertView The recycled view to be reused, or null if no recycled view is available.
     * @param parent The parent ViewGroup that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     *
     * @see Item
     * @see View
     * @see LayoutInflater
     * @see TextView
     * @see CheckBox
     * @see View.OnClickListener
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
        }

        Item item = items.get(position);
        TextView itemName = view.findViewById(R.id.item_name);
        TextView itemValue = view.findViewById(R.id.item_value);
        TextView itemModel = view.findViewById(R.id.item_model);
        TextView itemMake = view.findViewById(R.id.item_make);

        itemName.setText(item.getName());
        itemModel.setText(item.getModel());
        itemMake.setText(item.getMake());

        // Value field has 2 cases:
        // if quantity = 1, then just show the value of one instance of Item
        // if quantity > 1, then show value of one instance of Item + number of Items
        Integer q = item.getQuantity();
        if (q == 1){
            @SuppressLint("DefaultLocale") String value = String.format("$%.2f", item.getEstimatedValue());
            itemValue.setText(value);
        } else{
            @SuppressLint("DefaultLocale") String value = String.format("$%.2f x %d", item.getEstimatedValue(), q);
            itemValue.setText(value);
        }

        CheckBox checkBox = view.findViewById(R.id.checkbox);
        // Hide checkboxes by default
        checkBox.setVisibility(multiSelectMode ? View.VISIBLE : View.GONE);

        // Handle checkbox selection
        checkBox.setChecked(isItemChecked(position));

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = checkBox.isChecked();
                itemSelection.put(position, isChecked);
                if (isChecked) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
            }
        });
        return view;
    }

    private boolean isItemChecked(int position) {
        return itemSelection.containsKey(position) && itemSelection.get(position);
    }

    /**
     * Clears the list of selected items and notifies the adapter of the data set change.
     */
    public void clearSelectedItems() {
        selectedItems.clear();
        itemSelection.clear();
        notifyDataSetChanged();
    }

    /**
     * takes all of the items in data list and adds them to the selected item list
     */
    public void selectAll() {
        selectedItems.clear();
        for (int i = 0; i < items.size(); i++) {
            itemSelection.put(i, true);
            selectedItems.add(items.get(i));
        }
        notifyDataSetChanged();
    }

}
