package com.example.a390project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

public class ProfileActivity extends AppCompatActivity {

    private final static String TAG = "ProfileActivity";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String name;
    private String id;

    //edit fields
    private TextView NAME;
    private TextView EMAIL;
    private TextView PASSWORD;
    
    //textview labels
    private TextView logInLabel;
    private TextView editLabel;

    private TextView nameLabel;
    private TextView emailLabel;
    private TextView passwordLabel;

    private Switch switch1;

    //once verified
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = mAuth.getInstance().getCurrentUser();
        id = mAuth.getUid();
        getname();
        
        logInLabel = findViewById(R.id.title_profile);
        editLabel = findViewById(R.id.title_profile_edit);

        mEmail = findViewById(R.id.email_edit_text_profile);
        mPassword = findViewById(R.id.password_edit_text_profile);
        mLogin = findViewById(R.id.LogInButton_profile);

        nameLabel = findViewById(R.id.TV);
        emailLabel = findViewById(R.id.TV2);
        passwordLabel = findViewById(R.id.TV3);
        NAME = findViewById(R.id.NameET);
        EMAIL = findViewById(R.id.EmailTV);
        PASSWORD = findViewById(R.id.PasswordTV);
        switch1 = findViewById(R.id.switch1);

        editLabel.setVisibility(View.GONE);
        nameLabel.setVisibility(View.GONE);
        emailLabel.setVisibility(View.GONE);
        passwordLabel.setVisibility(View.GONE);
        NAME.setVisibility(View.GONE);
        EMAIL.setVisibility(View.GONE);
        PASSWORD.setVisibility(View.GONE);
        switch1.setVisibility(View.GONE);
        
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String [] emailLogin = new String [1];
                emailLogin[0] = mEmail.getText().toString().trim();
                final String [] passwordLogin = new String [1];
                passwordLogin[0] = mPassword.getText().toString().trim();

                if (!emailLogin[0].isEmpty() && !passwordLogin[0].isEmpty()) {
                    FirebaseUser testUser = user;
                    // Get auth credentials from the user for re-authentication
                    AuthCredential credential = EmailAuthProvider.getCredential(emailLogin[0], passwordLogin[0]); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    testUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User re-authenticated.");

                                logInLabel.setVisibility(View.GONE);
                                mEmail.setVisibility(View.GONE);
                                mPassword.setVisibility(View.GONE);
                                mLogin.setVisibility(View.GONE);

                                editLabel.setVisibility(View.VISIBLE);
                                nameLabel.setVisibility(View.VISIBLE);
                                emailLabel.setVisibility(View.VISIBLE);
                                passwordLabel.setVisibility(View.VISIBLE);
                                NAME.setVisibility(View.VISIBLE);
                                EMAIL.setVisibility(View.VISIBLE);
                                PASSWORD.setVisibility(View.VISIBLE);
                                switch1.setVisibility(View.VISIBLE);

                                EMAIL.setText(emailLogin[0]);
                                PASSWORD.setText(passwordLogin[0]);

                                switch1.setChecked(true);
                                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            NAME.setEnabled(true);
                                            EMAIL.setEnabled(true);
                                            PASSWORD.setEnabled(true);
                                        } else if (!isChecked) {
                                            final String name = NAME.getText().toString().trim();
                                            final String email = EMAIL.getText().toString().trim();
                                            final String password = PASSWORD.getText().toString().trim();

                                            if (!name.isEmpty()) {
                                                setname(name);
                                            }

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            if (!email.equals(emailLogin)) {
                                                user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful() && !email.isEmpty()) {
                                                            setemail(email);
                                                            Log.d(TAG, "User email address updated.");
                                                        } else {
                                                            Log.d(TAG, "User email address not updated.");
                                                        }
                                                    }
                                                });
                                            }
                                            if (!password.equals(passwordLogin)) {
                                                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful() && !password.isEmpty()) {
                                                            Log.d(TAG, "User password updated.");
                                                        } else {
                                                            Log.d(TAG, "User password not updated.");
                                                        }
                                                    }
                                                });
                                                NAME.setEnabled(false);
                                                EMAIL.setEnabled(false);
                                                PASSWORD.setEnabled(false);
                                            }
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(ProfileActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            }
        });
    }

    private void getname(){
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rootRef.child("users").removeEventListener(this);
                String n = dataSnapshot.child(id).child("name").getValue(String.class);
                name = n.trim();
                NAME.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void setname(String n){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("users").child(id).child("name").setValue(n);
    }
    private void setemail(String n){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("users").child(id).child("email").setValue(n);
    }


}