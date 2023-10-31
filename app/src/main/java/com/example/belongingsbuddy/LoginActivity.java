package com.example.belongingsbuddy;

import android.os.Bundle;
import android.widget.Button;

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
    }
}
