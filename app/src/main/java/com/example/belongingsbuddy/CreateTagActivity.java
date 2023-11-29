package com.example.belongingsbuddy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreateTagActivity extends DialogFragment {
    ArrayAdapter<Tag> TagAdapter;
    View dialogView;
    EditText addTagView;
    GridView tagListView;
    Button Confirm;
    Button Cancel;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        dialogView= LayoutInflater.from(getActivity()).inflate(R.layout.tag_fragment2, null);
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(dialogView);

        // Get references to your views
        addTagView = dialogView.findViewById(R.id.addView);
        tagListView = dialogView.findViewById(R.id.elementListView);

        // Initialize your tag data and the custom adapter
        TagManager manager = (TagManager) getArguments().getSerializable("Manager");
        Set<Tag> tags = manager.getTags();
        TagAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1);
        TagAdapter.addAll(tags);
        tagListView.setAdapter(TagAdapter);
        addTagView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN)) {
                    TagAdapter.add(new Tag(addTagView.getText().toString()));

                    return true; // Consume the event
                }
                return false;
            }
        });

        Confirm = dialogView.findViewById(R.id.btnConfirm);
        Cancel = dialogView.findViewById(R.id.btnCancel);
        //Upon confirm grab all the checked tags and overwrite the old one with the new ones and display them
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Tag> tagSet = new HashSet<>();
                for(int i = 0; i < TagAdapter.getCount(); i++){
                    tagSet.add(TagAdapter.getItem(i));
                }
                manager.setTags(tagSet);
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
}
