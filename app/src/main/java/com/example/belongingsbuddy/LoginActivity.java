package com.example.belongingsbuddy;

import static com.google.firebase.FirebaseError.ERROR_EMAIL_ALREADY_IN_USE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth myAuth;
    private Button login_button;
    private Button sign_up_button;
    private EditText username_input;
    private EditText password_input;
    private Button login_confirm_button;
    private Button sign_up_confirm_button;
    private Button back_button;

    /**
     * toasts a given text on the current activity screen
     * @param text
     *      a string that will be the message in the toast
     */
    private void toastFunction(String text) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    /**
     * changes visibility of buttons and EditText features to change the view to
     * the view seen when inputting username and password
     */
    private void setLoginSignupVisibility() {
        login_button.setVisibility(View.GONE);
        sign_up_button.setVisibility(View.GONE);
        username_input.setVisibility(View.VISIBLE);
        password_input.setVisibility(View.VISIBLE);
        back_button.setVisibility(View.VISIBLE);
        username_input.setText("");
        password_input.setText("");
    }

    /**
     * changes visibility of buttons and EditText features to "reset" the view back to
     * the initial login screen
     */
    private void setStartScreenVisibility() {
        login_button.setVisibility(View.VISIBLE);
        sign_up_button.setVisibility(View.VISIBLE);
        username_input.setVisibility(View.GONE);
        password_input.setVisibility(View.GONE);
        back_button.setVisibility(View.GONE);
        login_confirm_button.setVisibility(View.GONE);
        sign_up_confirm_button.setVisibility(View.GONE);
        username_input.setText("");
        password_input.setText("");
    }

    /**
     * Initializes the activity when it is created
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = findViewById(R.id.login_button);
        sign_up_button = findViewById(R.id.create_button);
        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.password_input);
        back_button = findViewById(R.id.back_button);
        sign_up_confirm_button = findViewById(R.id.sign_up_confirm);
        login_confirm_button = findViewById(R.id.login_confirm);

        login_button.setOnClickListener(new View.OnClickListener() {
            /**
             * calls setLoginSignupVisibility changing the view to the screen where
             * the username and password inputs are visible
             * also makes the login confirm button visible
             * @param v The login button that was clicked
             */
            @Override
            public void onClick(View v) {
                setLoginSignupVisibility();
                login_confirm_button.setVisibility(View.VISIBLE);
            }
        });

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            /**
             * calls setLoginSignupVisibility changing the view to the screen where
             * the username and password inputs are visible
             * also makes the sign up confirm button visible
             * @param v The signup button that was clicked
             */
            @Override
            public void onClick(View v) {
                setLoginSignupVisibility();
                sign_up_confirm_button.setVisibility(View.VISIBLE);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            /**
             * reverts the screen back to the initial LoginActivity screen
             * (calls setStartScreenVisibility)
             * @param v The back_button that was clicked
             */
            @Override
            public void onClick(View v) {
                setStartScreenVisibility();
            }
        });

        sign_up_confirm_button.setOnClickListener(new View.OnClickListener() {
            /**
             * receives the username (email) and password inputted by user
             * creates and authenticates the new account in the FirestoreAuthentication system using the user input
             * prevents invalid user input (empty username/password) (invalid username (not email format))
             * switches to the main activity once new account created and signed into
             * @param v The signup button that was clicked
             */
            @Override
            public void onClick(View v) {
                String username = username_input.getText().toString();
                String password = password_input.getText().toString();
                myAuth = FirebaseAuth.getInstance();

                // user input validation
                if (username.isEmpty() && password.isEmpty()) {
                    toastFunction(getString(R.string.invalid_username_and_password));
                } else if (username.isEmpty()) {
                    toastFunction(getString(R.string.invalid_email));
                } else if (password.isEmpty()) {
                    toastFunction(getString(R.string.invalid_password));
                } else {
                    // create new account in FirebaseAuth using user input
                    myAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                toastFunction(getString(R.string.successfully_signed_up));
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(i);
                            } else {
                                try {
                                    throw ((FirebaseAuthException) task.getException());
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    toastFunction(getString(R.string.weak_password));
                                    if (myAuth.getCurrentUser() != null) {
                                        myAuth.getCurrentUser().delete();
                                    }
                                } catch(FirebaseAuthUserCollisionException e) {
                                    toastFunction(getString(R.string.account_collision));
                                    if (myAuth.getCurrentUser() != null) {
                                        myAuth.getCurrentUser().delete();
                                    }
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    if (e.getErrorCode().equals(getString(R.string.error_invalid_email))) {
                                        toastFunction(getString(R.string.invalid_email));
                                    }
                                    if (myAuth.getCurrentUser() != null) {
                                        myAuth.getCurrentUser().delete();
                                    }
                                } catch(FirebaseAuthException e) {
                                    Log.v(getString(R.string.autherror), e.getMessage());
                                    toastFunction(getString(R.string.internal_login_error));
                                    if (myAuth.getCurrentUser() != null) {
                                        myAuth.getCurrentUser().delete();
                                    }
                                } catch (Exception e) {
                                    if (e.getMessage().equals(getString(R.string.network_error_code))) {
                                        toastFunction(getString(R.string.network_error));

                                    } else {
                                        Log.v(getString(R.string.autherror), e.getMessage());
                                        toastFunction(getString(R.string.internal_login_error));
                                    }
                                    if (myAuth.getCurrentUser() != null) {
                                        myAuth.getCurrentUser().delete();
                                    }
                                }
                            }
                        }
                    });
                    myAuth.createUserWithEmailAndPassword(username, password);
                }
            }
        });
        login_confirm_button.setOnClickListener(new View.OnClickListener() {
            /**
             * receives the username (email) and password inputted by user
             * compares the user input to the (automatically) encrypted accounts stored in the FirebaseAuthentication system
             * prevents invalid user input (empty username/password) (invalid username (not email format))
             * switches to the main activity if user input matches existing account
             * returns to initial login activity if user input does not match
             * @param v The login button that was clicked
             */
            @Override
            public void onClick(View v) {
                String username = username_input.getText().toString();
                String password = password_input.getText().toString();
                myAuth = FirebaseAuth.getInstance();
                // user input validation
                if (username.isEmpty() && password.isEmpty()) {
                    toastFunction(getString(R.string.invalid_username_and_password));
                } else if (username.isEmpty()) {
                    toastFunction(getString(R.string.invalid_username));
                } else if (password.isEmpty()) {
                    toastFunction(getString(R.string.invalid_password));
                } else {
                    //Attempt to login to existing account in FirebaseAuth
                    myAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(i);
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    if (e.getErrorCode().equals(getString(R.string.error_invalid_email))) {
                                        toastFunction(getString(R.string.invalid_email));
                                    }
                                } catch (FirebaseException e) {
                                    if (e.getMessage().equals(getString(R.string.invalid_login_credentials))) {
                                        toastFunction(getString(R.string.incorrect_username_or_password));
                                    } else if (e.getMessage().equals(getString(R.string.failed_login_overload))) {
                                        toastFunction(getString(R.string.failed_attempts_timeout));
                                    } else {
                                        toastFunction(getString(R.string.internal_login_error));
                                    }
                                } catch (Exception e) {
                                    Log.v(getString(R.string.internalerror), e.getMessage());
                                    toastFunction(getString(R.string.internal_login_error));
                                }
                            }
                        }
                    });
                    myAuth.signInWithEmailAndPassword(username, password);
                }
            }
        });
    }
}
