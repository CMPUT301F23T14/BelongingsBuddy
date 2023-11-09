package com.example.belongingsbuddy;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class TagActivity extends DialogFragment {
    TagManager manager;
    ArrayAdapter<Tag> TagAdapter;
    View dialogView;
    SearchView searchView;
    GridView tagListView;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        dialogView= LayoutInflater.from(getActivity()).inflate(R.layout.tag_fragment, null);
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(dialogView);

        // Get references to your views
        searchView = dialogView.findViewById(R.id.searchView);
        tagListView = dialogView.findViewById(R.id.elementListView);

        // Initialize your tag data and the custom adapter
        // Here, you might want to load tags from a data source
        manager = new TagManager();
        manager.addTag(new Tag("Hello"));
        manager.addTag(new Tag("World"));
        manager.addTag(new Tag("How"));
        manager.addTag(new Tag("Are"));
        manager.addTag(new Tag("You"));
        manager.addTag(new Tag("what"));
        manager.addTag(new Tag("can"));
        manager.addTag(new Tag("they"));
        manager.addTag(new Tag("tell"));
        manager.addTag(new Tag("me"));
        ArrayList<Tag> TagList = new ArrayList(manager.getTags());

        TagAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_multiple_choice);
        TagAdapter.addAll(TagList);
        tagListView.setAdapter(TagAdapter);

// Configure the search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TagAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TagAdapter.getFilter().filter(newText);
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

// Show the dialog
        dialog.show();

        return dialog;
    }
}
