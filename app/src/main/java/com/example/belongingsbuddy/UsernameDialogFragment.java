package com.example.belongingsbuddy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Displays a dialog with two buttons and a title
 *
 * the "Sign Out" button will bring the user back to the beginning of the Login Activity
 * Will also sign them out of FirebaseAuth removing them as current user
 *
 * the "Cancel" Button returns to MainActivity
 */
public class UsernameDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle myArg = getArguments();
        String username = myArg.getString("username");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_username, null);

        TextView title = new TextView(getActivity());
        title.setText(username);
        title.setPadding(10, 80, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(36);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setCustomTitle(title)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Sign Out", (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                }).create();
    }
}
