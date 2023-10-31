package com.example.belongingsbuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference user_collection;
    private boolean sign_up_mode;
    private Button login_button;
    private Button sign_up_button;
    private EditText username_input;
    private EditText password_input;
    private Button login_confirm_button;
    private Button sign_up_confirm_button;
    private Button back_button;

    private boolean isSign_up_mode() {
        return sign_up_mode;
    }

    private void setSign_up_mode(boolean sign_up_mode) {
        this.sign_up_mode = sign_up_mode;
    }

    private void setLoginSignupVisibility() {
        getLogin_button().setVisibility(View.GONE);
        getSign_up_button().setVisibility(View.GONE);
        getUsername_input().setVisibility(View.VISIBLE);
        getPassword_input().setVisibility(View.VISIBLE);
        getBack_button().setVisibility(View.VISIBLE);
    }

    private void setStartScreenVisibility() {
        getLogin_button().setVisibility(View.VISIBLE);
        getSign_up_button().setVisibility(View.VISIBLE);
        getUsername_input().setVisibility(View.GONE);
        getPassword_input().setVisibility(View.GONE);
        getBack_button().setVisibility(View.GONE);
        getLogin_confirm_button().setVisibility(View.GONE);
        getSign_up_confirm_button().setVisibility(View.GONE);
    }

    private Button getLogin_button() {
        return login_button;
    }

    private Button getSign_up_button() {
        return sign_up_button;
    }

    private EditText getUsername_input() {
        return username_input;
    }

    private EditText getPassword_input() {
        return password_input;
    }

    private Button getBack_button() {
        return back_button;
    }

    private Button getLogin_confirm_button() {
        return login_confirm_button;
    }

    private Button getSign_up_confirm_button() {
        return sign_up_confirm_button;
    }

    public CollectionReference getUser_collection() {
        return user_collection;
    }



    private void setLogin_button(Button login_button) {
        this.login_button = login_button;
    }

    private void setSign_up_button(Button sign_up_button) {
        this.sign_up_button = sign_up_button;
    }

    public void setUser_collection(CollectionReference user_collection) {
        this.user_collection = user_collection;
    }

    private void setUsername_input(EditText username_input) {
        this.username_input = username_input;
    }

    private void setPassword_input(EditText password_input) {
        this.password_input = password_input;
    }

    private void setLogin_confirm_button(Button login_confirm_button) {
        this.login_confirm_button = login_confirm_button;
    }

    private void setSign_up_confirm_button(Button sign_up_confirm_button) {
        this.sign_up_confirm_button = sign_up_confirm_button;
    }

    private void setBack_button(Button back_button) {
        this.back_button = back_button;
    }

    private String byteToHex(byte b) {
        char[] hex_characters = new char[2];
        hex_characters[0] = Character.forDigit((b >> 4) & 0xF, 16);
        hex_characters[1] = Character.forDigit((b & 0xF), 16);
        return new String(hex_characters).toUpperCase();
    }

    private String getHex(byte[] bytes) {
        StringBuffer hex_string = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            hex_string.append(byteToHex(bytes[i]));
        }
        return hex_string.toString();
    }

    private String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 800;
        char[] password_chars = password.toCharArray();
        byte[] salt = getSalt();
        PBEKeySpec key_spec = new PBEKeySpec(password_chars, salt, iterations);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(key_spec).getEncoded();
        return getHex(hash);
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        setLogin_button(findViewById(R.id.login_button));
        setSign_up_button(findViewById(R.id.create_button));
        setUsername_input(findViewById(R.id.username_input));
        setPassword_input(findViewById(R.id.password_input));
        setBack_button(findViewById(R.id.back_button));
        setSign_up_confirm_button(findViewById(R.id.sign_up_confirm));
        setLogin_confirm_button(findViewById(R.id.login_confirm));

        getLogin_button().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginSignupVisibility();
                getLogin_confirm_button().setVisibility(View.VISIBLE);
                setSign_up_mode(false);
            }
        });

        getSign_up_button().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginSignupVisibility();
                getSign_up_confirm_button().setVisibility(View.VISIBLE);
                setSign_up_mode(true);
            }
        });

        getBack_button().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartScreenVisibility();
            }
        });

        View.OnClickListener signup_login_confirm_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = getUsername_input().getText().toString();
                String password = getPassword_input().getText().toString();
                int duration = Toast.LENGTH_LONG;
                if (username.isEmpty() && password.isEmpty()) {
                    CharSequence text = "Username and Password cannot be empty!";
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                } else if (username.isEmpty()) {
                    CharSequence text = "Username cannot be empty!";
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                } else if (password.isEmpty()) {
                    CharSequence text = "Username cannot be empty!";
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                } else {
                    try {
                        String hashed_username = generatePasswordHash(username);
                        String hashed_password = generatePasswordHash(password);
                        setUser_collection(db.collection("users"));
                        getUser_collection().document(hashed_username).set(hashed_password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        getSign_up_confirm_button().setOnClickListener(signup_login_confirm_listener);
        getLogin_confirm_button().setOnClickListener(signup_login_confirm_listener);
    }
}
