package com.example.belongingsbuddy;

import android.content.Context;
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

/**
 * This is sourced from listycity adapter.
 * It is not final.
 */
public class CustomList extends ArrayAdapter<Item> {
    private final Context context;
    private ArrayList<Item> items;
    private boolean multiSelectMode;
    private ArrayList<Item> selectedItems;
    //
    private int total;
    // constructor
    public CustomList(Context context, ArrayList<Item> dataList) {
        super(context, 0, dataList);
        this.items = dataList;
        this.context = context;
        this.total = sumValues(dataList);
        this.multiSelectMode = false;
        this.selectedItems = new ArrayList<>();
    }
    public int getTotal() {
        return total;
    }
    // there could be a better way maybe?
    private int sumValues(@NonNull ArrayList<Item> dataList) {
        int sum = 0;
        for(int i = 0; i < dataList.size(); i++)
            sum += dataList.get(i).getEstimatedValue();

        return sum;
    }
    public void setMultiSelectMode(boolean mode) {
        multiSelectMode = mode;
        selectedItems.clear();
        notifyDataSetChanged();
    }


    public ArrayList<Item> getSelectedItems() {
        return selectedItems;
    }

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

        itemName.setText(item.getName());
        itemValue.setText(item.getEstimatedValue().toString());
        CheckBox checkBox = view.findViewById(R.id.checkbox);
// Hide checkboxes by default
        checkBox.setVisibility(multiSelectMode ? View.VISIBLE : View.GONE);


// Handle checkbox selection
        checkBox.setChecked(selectedItems.contains(item));


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
            }
        });
        return view;
    }

    public void clearSelectedItems() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

}
