package com.example.belongingsbuddy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagActivity extends DialogFragment {
    ArrayAdapter<Tag> TagAdapter;
    View dialogView;
    SearchView searchView;
    GridView tagListView;
    Button Confirm;
    Button Cancel;
    TagListener mainActivity;
    ArrayList<Tag> selectedTags;
    HashMap<Tag, Boolean> TagBool;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        dialogView= LayoutInflater.from(getActivity()).inflate(R.layout.tag_fragment, null);
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(dialogView);

        // Get references to your views
        searchView = dialogView.findViewById(R.id.searchView);
        tagListView = dialogView.findViewById(R.id.elementListView);

        tagListView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        // Initialize your tag data and the custom adapter
        TagManager manager = (TagManager) getArguments().getSerializable("tagManager");

        selectedTags = (ArrayList<Tag>) getArguments().getSerializable("selectedTags");

        ArrayList<Tag> TagList = new ArrayList(manager.getTags());
        TagBool = new HashMap<>();

        TagAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_multiple_choice);
        TagAdapter.addAll(TagList);
        tagListView.setAdapter(TagAdapter);

        //Load previous data into the select list
        for(int i = 0; i < TagAdapter.getCount(); i++) {
            if(selectedTags.contains(TagAdapter.getItem(i))) {
                tagListView.setItemChecked(i, true);
                TagBool.put(TagAdapter.getItem(i), true);
            } else {
                tagListView.setItemChecked(i, false);
                TagBool.put(TagAdapter.getItem(i), false);
            }
        }

        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tag tagAtPosition = (Tag) tagListView.getItemAtPosition(position);
                TagBool.put(tagAtPosition,!TagBool.get(tagAtPosition));
            }
        });
// Configure the search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TagAdapter.getFilter().filter(newText);
                //Update checked list based on new filter
                updateList();
                return true;
            }
        });

// Set a positive button to handle the selection
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Handle the selected tags from the adapter
//                List<Tag> selectedTags = TagAdapter.getSelectedTags();
//                // Do something with the selected tags
//            }
//        });
        Confirm = dialogView.findViewById(R.id.btnConfirm);
        Cancel = dialogView.findViewById(R.id.btnCancel);
        //Upon confirm grab all the checked tags and overwrite the old one with the new ones and display them
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTags = new ArrayList<>();
                for (Map.Entry<Tag, Boolean> entry : TagBool.entrySet()) {
                    if(entry.getValue() == true) {
                        selectedTags.add(entry.getKey());
                    }
                    // ...
                }
                mainActivity.tagListen(selectedTags);
                dialog.dismiss();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();

        return dialog;
    }

    //Implementation to send data back from Dialog Fragment to the main activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mainActivity = (TagListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement TagListener");
        }
    }

    //Update the checks for the list based on configured search filters
    private void updateList() {
        for(int i = 0; i < tagListView.getCount(); i++) {
            if(TagBool.get(tagListView.getItemAtPosition(i)) == true) {
                tagListView.setItemChecked(i, true);
            } else {
                tagListView.setItemChecked(i, false);
            }
        }
    }
}
