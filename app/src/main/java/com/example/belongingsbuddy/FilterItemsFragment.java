package com.example.belongingsbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A {@link DialogFragment} subclass that represents a filter dialog for items in the Belongings Buddy app.
 * Users can set various filter criteria such as date range, keywords, makes, and tags to refine the displayed items.
 * Communicates with the hosting activity through the {@link Listener} interface.
 */
public class FilterItemsFragment extends DialogFragment implements TagListener {
    public Listener listener;
    View view;
    String startDate = null;
    String endDate = null;
    com.example.belongingsbuddy.Date startDateAsDate;
    com.example.belongingsbuddy.Date endDateAsDate;
    String[] keywords = {};
    String[] makes = {};
    ArrayList<Tag> selectedTagsArray = new ArrayList<>();

    // so we can communicate with main activity
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
     * Creates and returns a new {@link Dialog} for the filter items functionality. Inflates the layout from
     * {@code R.layout.filter_items}.
     *
     * The date picking functionality uses the MaterialDatePicker library for selecting a date range, and the selected
     * dates are displayed in a TextView.
     *
     * When the "OK" button is pressed, the entered filter settings (keywords, makes, tags, date range) are retrieved,
     * processed, and passed to the hosting activity using the {@link Listener} interface.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return A new instance of {@link Dialog} with the configured filter items view.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the view
        view = LayoutInflater.from(getActivity()).inflate(R.layout.filter_items, null);
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(view);

        // get ui elements
        TextView selectedDate = view.findViewById(R.id.filter_selected_dates);
        EditText selectedKeywords = view.findViewById(R.id.filter_selected_keywords);
        EditText selectedMakes = view.findViewById(R.id.filter_selected_makes);

        // click listeners for buttons
        // date
        Button date = view.findViewById(R.id.filter_pick_date_button);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("Select a date range");

                // Adding a date validator to ensure no future dates are selected
                builder.setCalendarConstraints(new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.now())
                        .build());

                // Building the date picker dialog
                MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
                datePicker.addOnPositiveButtonClickListener(selection -> {

                    // Retrieving the selected start and end dates
                    Long startDateLong = selection.first;
                    Long endDateLong = selection.second;

                    // Formating the selected dates as strings, note mm is not months is milliseconds
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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

        //Open tag selection interface for tag filter selection
        Button filterTags = view.findViewById(R.id.add_filter_tags_button);
        filterTags.setOnClickListener(v -> {
                TagManager manager = listener.getTagManager();
                manager.openTagSelector(this, getParentFragmentManager(), selectedTagsArray);
            });

        // ok
        Button ok = view.findViewById(R.id.filter_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all text written into edit texts
                // \s is not supported for some reason
                if (!selectedKeywords.getText().toString().isEmpty()) {
                    keywords = selectedKeywords.getText().toString().split("[\\t\\r\\n\\f ]*,[\\t\\r\\n\\f ]*");
                }
                if (!selectedMakes.getText().toString().isEmpty()) {
                    makes = selectedMakes.getText().toString().split("[\\t\\r\\n\\f ]*,[\\t\\r\\n\\f ]*");
                }
                if (startDate != null) {
                    startDateAsDate = new com.example.belongingsbuddy.Date(startDate);
                    endDateAsDate = new com.example.belongingsbuddy.Date(endDate);

                }
                listener.onFilterOkPressed(keywords, makes, selectedTagsArray, startDateAsDate, endDateAsDate);
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

    @Override
    public void tagListen(ArrayList<Tag> tagList) {
        String tagSequence = "";
        for(Tag tagString: tagList) {
            tagSequence += tagString + ", ";
        }
        TextView addTags = view.findViewById(R.id.filter_selected_tags);
        addTags.setText(tagSequence);
        selectedTagsArray = tagList;
    }
}