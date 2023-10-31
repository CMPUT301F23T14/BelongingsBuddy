package com.example.belongingsbuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        final Button login_button = findViewById(R.id.login_button);
        final Button sign_up_button = findViewById(R.id.create_button);
        final EditText username_input = findViewById(R.id.username_input);
        final EditText password_input = findViewById(R.id.password_input);
        final Button login_confirm_button = findViewById(R.id.login_confirm);
        final Button sign_up_confirm_button = findViewById(R.id.sign_up_confirm);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_button.setVisibility(View.GONE);
                sign_up_button.setVisibility(View.GONE);
                username_input.setVisibility(View.VISIBLE);
                password_input.setVisibility(View.VISIBLE);
                login_confirm_button.setVisibility(View.VISIBLE);

            }
        });
    }
}
