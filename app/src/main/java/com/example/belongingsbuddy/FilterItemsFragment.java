package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import com.google.android.material.datepicker.MaterialDatePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Displays a dialog with 4 selection buttons, cancel and confirm, and a radio group of 2
 * Whatever button is clicked last before hitting confirm will be the sort type to be performed.
 * Default order is ascending (alphabetical no other reason).
 * If the us presses cancel dialogue is dismissed, else they return to main and see sorted list.
 */
public class FilterItemsFragment extends DialogFragment {
    public Listener listener;
    String startDate = null;
    String endDate = null;
    ArrayList<String> keywords = new ArrayList<>();
    boolean isAscending = false;

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_items, null);
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(view);

        // get ui elements
        TextView selectedDate = view.findViewById(R.id.add_date_filter);
        TextView selectedKeywords = view.findViewById(R.id.filter_keywords);

        // click listeners for buttons
        // date
        Button date = view.findViewById(R.id.filter_pick_date_button);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("Select a date range");

                // Building the date picker dialog
                MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
                datePicker.addOnPositiveButtonClickListener(selection -> {

                    // Retrieving the selected start and end dates
                    Long startDateLong = selection.first;
                    Long endDateLong = selection.second;

                    // Formating the selected dates as strings
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    String startDateString = sdf.format(new Date(startDateLong));
                    String endDateString = sdf.format(new Date(endDateLong));

                    // getting dates for filtering
                    startDate = startDateString;
                    endDate = endDateString;

                    // Creating the date range string
                    String selectedDateRange = startDateString + " - " + endDateString;

                    // Displaying the selected date range in the TextView
                    selectedDate.setText(selectedDateRange);
                });

                // Showing the date picker dialog
                datePicker.show(getChildFragmentManager(), "DATE_PICKER");
            }
        });

//        // desc
//        Button desc = view.findViewById(R.id.filter_add_keyword);
//        desc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sortType = "desc";
//            }
//        });
//        // make
//        Button make = view.findViewById(R.id.filter_make);
//        make.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                sortType = "make";
//            }
//        });
//        // value
////        Button value = view.findViewById(R.id.filter_tag);
//        value.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                sortType = "value";
//            }
//        });
        // ok
        Button ok = view.findViewById(R.id.filter_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.onSortOKPressed(sortType, isAscending);
                dialog.dismiss();
            }
        });

        // cancel
        Button cancel = view.findViewById(R.id.filter_cancel);
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
