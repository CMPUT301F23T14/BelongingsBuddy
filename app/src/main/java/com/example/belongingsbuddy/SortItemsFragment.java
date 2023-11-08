package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Displays a dialog with 4 buttons and a radio group of 2
 * the user can select which type of sorting they'd like to perform
 * which will lead to another interface or will close the dialogue.
 * They can also return to main page with the cancel button.
 */
public class SortItemsFragment extends DialogFragment {
//    private OnFragmentInteractionListener listener;
    private String sortType = "NONE";
    private Boolean isAscending = true;
    public Listener listener;
    // so we can communicate with main activity
    public interface OnFragmentInteractionListener {
        void onSortOKPressed(String date, Boolean isAscending);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        }
        else {
            throw new RuntimeException("MUST IMPLEMENT Listener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sort_items, null);
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(view);

        // click listeners for buttons
        // date
        Button date = view.findViewById(R.id.sort_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = "date";
//                date.setBackgroundColor(0xe9e7fa);
            }
        });
        // desc
        Button desc = view.findViewById(R.id.sort_desc);
        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = "desc";
//                desc.setBackgroundColor(0xe9e7fa);
            }
        });
        // make
        Button make = view.findViewById(R.id.sort_make);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = "make";
//                make.setBackgroundColor(0xe9e7fa);
            }
        });
        // value
        Button value = view.findViewById(R.id.sort_value);
        value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = "value";
//                value.setBackgroundColor(0xe9e7fa);
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
                        isAscending = true;

                        break;
                    case 1: // descending
//                        Toast.makeText(getContext(), "Selected button number " + index, Toast.LENGTH_SHORT).show();
                        isAscending = false;
                        break;
                }
            }
        });
        // ok
        Button ok = view.findViewById(R.id.sort_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                date.setBackgroundColor(getResources().getColor(R.color.grey_button));
//                make.setBackgroundColor(getResources().getColor(R.color.grey_button));
//                desc.setBackgroundColor(getResources().getColor(R.color.grey_button));
//                value.setBackgroundColor(getResources().getColor(R.color.grey_button));
                listener.onSortOKPressed(sortType, isAscending);
                dialog.dismiss();
            }
        });

        // cancel
        Button cancel = view.findViewById(R.id.sort_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                date.setBackgroundColor(getResources().getColor(R.color.grey_button));
//                make.setBackgroundColor(getResources().getColor(R.color.grey_button));
//                desc.setBackgroundColor(getResources().getColor(R.color.grey_button));
//                value.setBackgroundColor(getResources().getColor(R.color.grey_button));
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }
}