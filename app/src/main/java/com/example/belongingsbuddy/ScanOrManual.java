package com.example.belongingsbuddy;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

/**
 * Displays a dialog with three buttons and a prompt;
 *
 * the "Scan barcode" Button will start an activity where the user
 * can scan barcode of an item they want to add to inventory;
 *
 * the "Input manually" Button will start an activity where the user
 * has to manually input the  information for a new item;
 *
 * the "Cancel" button will close the Dialog;
 */
public class ScanOrManual extends DialogFragment{
    public Listener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // set up the listener
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener is not implemented");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // get the View
        View view = View.inflate(getContext(), R.layout.scan_or_manual, null);
        // view = LayoutInflater.from(getActivity()).inflate(R.layout.scan_or_manual, null);
        final Dialog dialog = new Dialog(this.requireContext());
        //final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(view);

        // SCAN BARCODE implementation:
        Button scan = view.findViewById(R.id.scan_bar);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                requireActivity().startActivity(intent);
                dialog.dismiss();

            }
        });

        // INPUT MANUALLY implementation:
        Button manual = view.findViewById(R.id.input_manually);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.inputManually();
                dialog.dismiss();
            }
        });

        // CANCEL implementation
        Button cancel = view.findViewById(R.id.cancel_add);
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