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
 * Displays a dialog with 4 selection buttons, cancel and confirm, and a radio group of 2
 * Whatever button is clicked last before hitting confirm will be the sort type to be performed.
 * Default order is ascending (alphabetical no other reason).
 * If the us presses cancel dialogue is dismissed, else they return to main and see sorted list.
 */
public class SortItemsFragment extends DialogFragment {
//    private OnFragmentInteractionListener listener;
    private String sortType = "NONE";
    private Boolean isAscending = true;
    public Listener listener;
    // so we can communicate with main activity
    /**
     * Called when the fragment is attached to its host activity.
     * This method checks if the hosting activity implements the {@link Listener} interface.
     * If it does, it assigns the activity as the listener for communication.
     * If not, a {@link RuntimeException} is thrown.
     *
     * @param context context fragement uses (should be main activity)
     * @throws RuntimeException if the activity does not implement the {@link Listener} interface
     *
     * @see Listener
     */
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
    /**
     * Creates and returns a sorting dialog for the BelongingsBuddy application.
     * The dialog allows the user to choose the sorting criteria (date, description, make, or value)
     * and specify the sorting order (ascending or descending).
     *
     * @param savedInstanceState The bundle with the saved state of the fragment, or null if not available
     * @return a new instance of a dialogue for sorting items
     *
     * @see View
     * @see Dialog
     * @see Button
     * @see RadioGroup
     */
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