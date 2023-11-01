package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Displays a dialog with 4 buttons and a radio group of 2
 * the user can select which type of sorting they'd like to perform
 * which will lead to another interface or will close the dialogue.
 * They can also return to main page with the cancel button.
 */
public class SortItems extends DialogFragment {
    View view;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.sort_items, null);
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(view);

        // buttons
        // date range
        Button date = view.findViewById(R.id.sort_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // keywords
        Button keywords = view.findViewById(R.id.sort_keywords);
        keywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // make
        Button make = view.findViewById(R.id.sort_make);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // tags
        Button tags = view.findViewById(R.id.sort_tags);
        tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // radio group click listener
        final RadioGroup radio = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                View radioButton = radio.findViewById(checkedId);
                int index = radio.indexOfChild(radioButton);
                // choose ordering
                switch (index) {
                    case 0: // ascending
//                        Toast.makeText(getContext(), "Selected button number " + index, Toast.LENGTH_SHORT).show();
                        break;
                    case 1: // descending
//                        Toast.makeText(getContext(), "Selected button number " + index, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        // cancel
        Button cancel = view.findViewById(R.id.sort_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }
}