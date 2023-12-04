package com.example.belongingsbuddy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreateTagActivity extends DialogFragment {
    TagArrayAdapter TagAdapter;
    View dialogView;
    EditText addTagView;
    GridView tagListView;
    Button Confirm;
    Button Cancel;
    TagListener mainActivity;
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
        TagAdapter = new TagArrayAdapter(this.getContext(), R.layout.deletable_item, tags);
        tagListView.setAdapter(TagAdapter);
        //Listen for tag entry on keyboard
        addTagView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String text = addTagView.getText().toString();
                    if (!text.equals("")) {
                        TagAdapter.add(new Tag(text));
                        addTagView.setText("");
                    }
                    return true; // Consume the event
                }
                return false;
            }
        });

        //Listen for tag entry on the enter button
        Button enterButton = dialogView.findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = addTagView.getText().toString();
                if (!text.equals("")) {
                    TagAdapter.add(new Tag(text));
                    addTagView.setText("");
                }
                addTagView.setText("");
            }
        });

        Confirm = dialogView.findViewById(R.id.btnConfirm);
        Cancel = dialogView.findViewById(R.id.btnCancel);
        //Upon confirm grab all the checked tags and overwrite the old one with the new ones and display them
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Tag> tagList = new ArrayList<>();
                for(int i = 0; i < TagAdapter.getCount(); i++){
                    tagList.add(TagAdapter.getItem(i));
                }
                mainActivity.tagListen(tagList);
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

    //Set the calling activity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mainActivity = (TagListener) context;
        }
        else {
            throw new RuntimeException("MUST IMPLEMENT TagListener");
        }
    }
}
