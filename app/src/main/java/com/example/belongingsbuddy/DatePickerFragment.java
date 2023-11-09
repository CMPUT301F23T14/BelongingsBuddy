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

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        this.onAttach(getContext());
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        DatePickerDialog date = new DatePickerDialog(requireContext(), this, year, month, day);
        date.getDatePicker().setMaxDate(System.currentTimeMillis());
        return date;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date the user picks.
        Date date = new Date(day, month, year);
        TextView t = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getOwnerActivity()).findViewById(R.id.add_date);
        t.setText(date.getString());
        Objects.requireNonNull(getDialog()).dismiss();

    }


}