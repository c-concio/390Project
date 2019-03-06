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
import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.Employee;
import com.example.a390project.ListViewAdapters.EmployeeListViewAdapter;
import com.example.a390project.Model.User;
import com.example.a390project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

// Todo: Search button, authenticate only manager can view
// Todo: Fix up employee floating button

public class EmployeeFragment extends Fragment {

    private static final String TAG = "EmployeeFragment";

    private View view;
    ListView employeeListView;
    DummyDatabase db = new DummyDatabase();
    private List<Employee> employees;
    private String currentUserId;
    private FirebaseHelper firebaseHelper;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.employee_fragment, parent, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        this.view = view;

        setupUI();

        // get the users from firebase
        firebaseHelper.getEmployees(view);

    }

    // setup the components in the layout
    private void setupUI(){
        firebaseHelper = new FirebaseHelper();

        // find the views
        employeeListView = view.findViewById(R.id.employeesListView);

        Log.d(TAG, "setupUI: Current user's id = " + FirebaseAuth.getInstance().getCurrentUser().getUid());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // setup what happens when a list view item is clicked
        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // go to the employee's activity
                Intent intent = new Intent(view.getContext(), EmployeeActivity.class);
                intent.putExtra("employeeID", employees.get(position).getAccountID());
                startActivity(intent);
            }
        });
    }

    // setup the adapter for the employees list view
    private void listViewAdapter(){
        //Log.d(TAG, "updateUsers: updated name: " + employees.get(3));

        Log.d(TAG, "listViewAdapter: employees size = " + employees.size());
        Log.d(TAG, "listViewAdapter: --------------------------------------");
        for(Employee currentEmployee : employees){
            Log.d(TAG, "listViewAdapter: employee name = " + currentEmployee.getName());
        }
        Log.d(TAG, "listViewAdapter: --------------------------------------");

        EmployeeListViewAdapter employeeAdapter = new EmployeeListViewAdapter(view.getContext(), employees);
        employeeListView.setAdapter(employeeAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseHelper.detatchEmployeeChildEventListener();
    }
}
