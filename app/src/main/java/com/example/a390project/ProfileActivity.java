package com.example.a390project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    private String name;
    private String email;
    private String id;
    private String password;

    private TextView NAME;
    private TextView EMAIL;
    private TextView PASSWORD;

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
        PASSWORD = findViewById(R.id.PasswordTV);
        EMAIL = findViewById(R.id.EmailTV);
        switch1 = findViewById(R.id.switch1);

        NAME.setText(name);
        PASSWORD.setText("");
        EMAIL.setText(email);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    NAME.setEnabled(true);
                    PASSWORD.setEnabled(true);
                    EMAIL.setEnabled(true);
                }else if(isChecked == false){

                    update_user();
                    NAME.setEnabled(false);
                    PASSWORD.setEnabled(false);
                    EMAIL.setEnabled(false);
                }
            }
        });


    }
    private void Getname(String n){
        name = n;
    }

    private void getname(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child(id).child("name").getValue(String.class);
                Getname(n);
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
    private void update_user(){

        if(EMAIL.getText().toString() != null) {
            setemail(EMAIL.getText().toString());
            user.updateEmail(EMAIL.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Email is changed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email is not changed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(PASSWORD.getText().toString()!= null) {
            user.updatePassword(PASSWORD.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Password is changed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password is not changed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        setname(NAME.getText().toString());
    }


}

