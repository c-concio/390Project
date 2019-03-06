package com.example.a390project.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a390project.DialogFragments.CreateEmployeeDialogFragment;
import com.example.a390project.DummyDatabase;
import com.example.a390project.EmployeeActivity;
import com.example.a390project.Model.Employee;
import com.example.a390project.ListViewAdapters.EmployeeListViewAdapter;
import com.example.a390project.Model.User;
import com.example.a390project.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

// Todo: Search button, populate with users, authenticate only manager can view
// Todo: Fix up employee floating button

public class EmployeeFragment extends Fragment {

    private static final String TAG = "EmployeeFragment";

    private View view;
    private List<String> names = new ArrayList<>();
    ListView employeeListView;
    DummyDatabase db = new DummyDatabase();
    private List<Employee> employees;

    // variables needed to access firebase
    DatabaseReference dbRef;
    ChildEventListener childEventListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.employee_fragment, parent, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        this.view = view;

        setupUI();

        // get the users from firebase
        getUsers();

    }

    // setup the components in the layout
    private void setupUI(){
        // find the views
        employeeListView = view.findViewById(R.id.employeesListView);

        // get the database reference of the users in firebase
        dbRef = FirebaseDatabase.getInstance().getReference("users");

        // setup what happens when a list view item is clicked
        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // go to the employee's activity
                //Todo: put the user id into the activity
                /*Intent intent = new Intent(view.getContext(), EmployeeActivity.class);
                intent.putExtra("employeeID", users.get(position).getEmployeeID());
                startActivity(intent);*/
            }
        });
    }

    //Todo: populate the users list
    public void getUsers(){

        names = new ArrayList<>();
        employees = new ArrayList<>();
        childEventListener = new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getNames(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged: ");
                updateUsers(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: ");
                updateUsers(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved: ");
                updateUsers(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        dbRef.addChildEventListener(childEventListener);
    }

    public void getNames(DataSnapshot dataSnapshot){
        names.add(dataSnapshot.child("name").getValue(String.class));

        Log.d(TAG, "updateUsers: dataSnapshot return: " + dataSnapshot.getValue());
        Employee newEmployee = dataSnapshot.getValue(Employee.class);
        if (newEmployee != null) {
            newEmployee.setAccountID(dataSnapshot.getKey());
            Log.d(TAG, "updateUsers: account id: " + newEmployee.getAccountID());
        }
        employees.add(newEmployee);

        // update listView once a user has changed or added
        listViewAdapter();
    }

    public void updateUsers(DataSnapshot dataSnapshot){
        String employeeId = dataSnapshot.getKey();
        Log.d(TAG, "updateUsers: employeeId = " + employeeId);
        for(int i = 0; i < employees.size(); i++){
            Log.d(TAG, "updateUsers: employee name =" + employees.get(i).getAccountID());
            if (employeeId.equals(employees.get(i).getAccountID())){
                employees.set(i, dataSnapshot.getValue(Employee.class));
                employees.get(i).setAccountID(employeeId);
                Log.d(TAG, "updateUsers: updated name: " + employees.get(i).getName());
            }
        }
        listViewAdapter();
    }


    // setup the adapter for the employees list view
    private void listViewAdapter(){
        //Log.d(TAG, "updateUsers: updated name: " + employees.get(3));

        Log.d(TAG, "listViewAdapter: --------------------------------------");
        for(Employee currentEmployee : employees){
            Log.d(TAG, "listViewAdapter: employee name = " + currentEmployee.getName());
        }
        Log.d(TAG, "listViewAdapter: --------------------------------------");

        EmployeeListViewAdapter employeeAdapter = new EmployeeListViewAdapter(view.getContext(), names);
        employeeListView.setAdapter(employeeAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        dbRef.removeEventListener(childEventListener);
    }
}
