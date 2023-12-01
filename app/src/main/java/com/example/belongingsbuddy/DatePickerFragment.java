package com.example.belongingsbuddy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Intuitive Dialog from which the user may select a date from the calendar
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    /**
     * Create a calendar Dialog set to the current date
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return interactive Dialog in the form of a calendar form which the user may select a date
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        this.onAttach(getContext());
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        DatePickerDialog date = new DatePickerDialog(requireContext(), this, year, month, day);
        // set the MaxDate to the current dat, so that users cannot choose dates in the future
        date.getDatePicker().setMaxDate(System.currentTimeMillis());
        return date;
    }

    /**
     * Create a String representing the chosen date and set the appropriate TextView in the calling
     * activity to this string
     * @param view the picker associated with the dialog
     * @param year the selected year
     * @param month the selected month (0-11 for compatibility with
     *              {@link Calendar#MONTH})
     * @param day the selected day of the month (1-31, depending on
     *                   month)
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date the user picks.
        month += 1;
        Date date = new Date(day, month, year);
        TextView t = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getOwnerActivity()).findViewById(R.id.add_date);
        t.setText(date.getString());
        Objects.requireNonNull(getDialog()).dismiss();

    }
}