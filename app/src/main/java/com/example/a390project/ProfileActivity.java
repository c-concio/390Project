package com.example.a390project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private FirebaseUser user;

    private String name;
    private String email;
    private String id;

    private TextView NAME;
    private TextView EMAIL;

    private Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = mAuth.getInstance().getCurrentUser();
        id = mAuth.getUid();
        email = user.getEmail();
        getname();

        NAME = findViewById(R.id.NameET);
        EMAIL = findViewById(R.id.EmailTV);
        switch1 = findViewById(R.id.switch1);

        EMAIL.setText(email);
        switch1.setChecked(true);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NAME.setEnabled(true);
                    EMAIL.setEnabled(true);
                }
                else if(!isChecked){
                    update_user(NAME.getText().toString().trim(), EMAIL.getText().toString().trim());
                    NAME.setEnabled(false);
                    EMAIL.setEnabled(false);
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
    private void update_user(String NAME, final String email){
        if(!email.isEmpty()) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Get auth credentials from the user for re-authentication
            AuthCredential credential = EmailAuthProvider
                    .getCredential("user@example.com", "password1234"); // Current Login Credentials \\
            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "User re-authenticated.");
                            //Now change your email address \\
                            //----------------Code for Changing Email Address----------\\
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                setemail(email);
                                                Log.d(TAG, "User email address updated.");
                                            }
                                        }
                                    });
                            //----------------------------------------------------------\\
                        }
                    });
        }
        if (!NAME.isEmpty()) {
            setname(NAME.trim());
        }
    }


}

