package com.example.a390project;

import android.util.Log;

import com.example.a390project.Model.Employee;
import com.example.a390project.Model.EmployeeTasks;
import com.example.a390project.Model.Machine;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class DatabaseHelper {

    private DatabaseReference machinedb;
    String TAG = "exeption";
    public DatabaseHelper(){//constructor
        try {
            machinedb = FirebaseDatabase.getInstance().getReference("Machine");//constructor initialises database name
        }
        catch(Exception e){
            Log.d(TAG,"exception"+e);
        }
    }
    public void add_machine(String name){

        String id = machinedb.push().getKey();//gets id from database
        Machine M = new Machine(id, name,"employee",true);//creates mcahine

        machinedb.child(id).setValue(M);//adds machine to database
    }
//    public List<Machine> getEmployees(int size){
//        List<Machine> emachine = new ArrayList<>();
//
//        for (int i = 1; i <= size; i++){
//            machinedb.add(new Employee(i, "Employee Name " + i));
//        }
//
//        return machine;
//    }


}