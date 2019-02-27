package com.example.a390project;

import android.util.Log;

import com.example.a390project.Model.Employee;
import com.example.a390project.Model.EmployeeTasks;
import com.example.a390project.Model.Machine;
import com.example.a390project.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class FirebaseHelper {

    private DatabaseReference rootRef;
    private String uId;

    public FirebaseHelper() {
        rootRef = FirebaseDatabase.getInstance().getReference();
        uId = FirebaseAuth.getInstance().getUid();
    }

    /*
   ------------------------------------------FirebaseHelper User creation functions-----------------------------------
    */
    public void addUser(String name, String email, boolean isManager, FirebaseUser currentUser) {
        Map<String, String> t = ServerValue.TIMESTAMP;
        uId = currentUser.getUid();
        rootRef.child("users").child(uId).setValue(new User(name, email, t, isManager));
    }

    /*
   ------------------------------------------FirebaseHelper Machine creation functions-----------------------------------
    */

    public void add_machine(String name){

        DatabaseReference machinedb;
        machinedb = FirebaseDatabase.getInstance().getReference("Machine");//constructor initialises database name
        String id = machinedb.push().getKey();//gets id from database
        Machine M = new Machine(id, name,"employee",true);//creates mcahine

        machinedb.child(id).setValue(M);//adds machine to database
    }


}