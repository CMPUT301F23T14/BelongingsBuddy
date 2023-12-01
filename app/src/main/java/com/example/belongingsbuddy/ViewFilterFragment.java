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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A {@link DialogFragment} subclass that represents a filter dialog for items in the Belongings Buddy app.
 * Users can set various filter criteria such as date range, keywords, makes, and tags to refine the displayed items.
 * Communicates with the hosting activity through the {@link Listener} interface.
 */
public class ViewFilterFragment extends DialogFragment {
    public Listener listener;

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_filter_layout, null);
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(view);

        // ok
        Button ok = view.findViewById(R.id.view_filter_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
}