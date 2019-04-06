package com.example.a390project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    public static final String TAG = "LogInActivity";

    //views
    private EditText mEmail;
    private EditText mPassword;
    private Button mButton;
    private TextView mLink;
    private ProgressBar mProgressBar;
    //variables
    private String email;
    private String password;

    //firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "onCreate: opened logInActivity");
        prepareActivity();
    }

    private void prepareActivity() {
        mEmail = findViewById(R.id.email_edit_text_register);
        mPassword = findViewById(R.id.password_edit_text_register);
        mButton = findViewById(R.id.LogInButton);
        mLink = findViewById(R.id.link_signup_text_view);
        mProgressBar = findViewById(R.id.progress_bar_log_in);
        mProgressBar.setVisibility(View.GONE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs(mEmail.getText().toString(), mPassword.getText().toString()))
                    loginFirebaseUser();
            }
        });

        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkInputs(String email, String password) {

        boolean emailCheck = false, passwordCheck = false;

        if (!email.isEmpty()) {
            this.email = email;
            emailCheck = true;
        }
        else
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();

        if(!password.isEmpty()) {
            this.password = password;
            passwordCheck = true;
        }
        else
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();

        if(emailCheck && passwordCheck) {
            //if fields are not-null, attempt to register
            return true;
        }
        else
            return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Log.d(TAG,"No user registered");
        }
        else {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void loginFirebaseUser() {
        mProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        mProgressBar.setVisibility(View.GONE);

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        updateUI(currentUser);

                        // ...
                    }
                });
    }

}
