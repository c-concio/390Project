package com.example.a390project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private final static String TAG = "RegisterActivity";
    //views
    private EditText mEmail;
    private EditText mFullName;
    private EditText mPassword;
    private ProgressBar mProgressBar;
    private Button mRegisterButton;

    private Button managerSecretButton;
    private int counter = 0;
    private Switch mSwitch;

    //variables
    private String email;
    private String fullName;
    private String password;
    private boolean isManager = false;


    //firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.email_edit_text_register);
        mFullName = findViewById(R.id.name_edit_text_register);
        mPassword = findViewById(R.id.password_edit_text_register);

        mProgressBar = findViewById(R.id.progress_bar_register);
        mSwitch = findViewById(R.id.manager_switch);
        managerSecretButton = findViewById(R.id.manager_secret_button);

        mProgressBar.setVisibility(View.GONE);
        mSwitch.setVisibility(View.GONE);

        mRegisterButton = findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInputs(mEmail.getText().toString().trim(), mFullName.getText().toString().trim(), mPassword.getText().toString()))
                    registerFirebaseUser();
            }
        });
        managerSecretButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                if (counter >= 5) {
                    mSwitch.setVisibility(View.VISIBLE);
                    counter = 0;
                }

            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isManager = isChecked;
            }
        });

    }

    private boolean checkInputs(String email, String fullName, String password) {

        boolean emailCheck = false, fullNameCheck = false, passwordCheck = false;

        if (!email.isEmpty()) {
            this.email = email;
            emailCheck = true;
        }
        else
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();

        if (!fullName.isEmpty()) {
            this.fullName = fullName;
            fullNameCheck = true;
        }
        else
            Toast.makeText(this, "Invalid full name", Toast.LENGTH_SHORT).show();

        if(!password.isEmpty()) {
            this.password = password;
            passwordCheck = true;
        }
        else
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();

        if(emailCheck && fullNameCheck && passwordCheck) {
            //if fields are not-null, attempt to register
            return true;
        }
        else
            return false;
    }

    private void registerFirebaseUser() {
        mProgressBar.setVisibility(View.VISIBLE);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseHelper firebaseHelper = new FirebaseHelper();
                            firebaseHelper.addUser(fullName, email, isManager, user);
                            updateUI(user);

                            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Email already in existance", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        mProgressBar.setVisibility(View.GONE);
        if (currentUser == null) {
            Log.d(TAG,"User not registered");
            Toast.makeText(this, "User not registered", Toast.LENGTH_SHORT).show();

        }
        else {

            Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
