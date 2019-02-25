package com.example.a390project.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a390project.DummyDatabase;
import com.example.a390project.EmployeeActivity;
import com.example.a390project.Model.Employee;
import com.example.a390project.ListViewAdapters.EmployeeListViewAdapter;
import com.example.a390project.R;

import java.util.List;

public class EmployeeFragment extends Fragment {

    private static final String TAG = "EmployeeFragment";

    private View view;
    private List<Employee> employees;
    ListView employeeListView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.employee_fragment, parent, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        this.view = view;

        setupUI();

        // for testing purposes, create a list of employees with dummy values
        employees = new DummyDatabase().generateDummyEmployees(25);

        listViewAdapter();


    }

    // setup the components in the layout
    private void setupUI(){
        // find the views
        employeeListView = view.findViewById(R.id.employeesListView);

        // setup what happens when a list view item is clicked
        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // go to the employee's activity
                Intent intent = new Intent(view.getContext(), EmployeeActivity.class);
                intent.putExtra("employeeID", employees.get(position).getEmployeeID());
                startActivity(intent);
            }
        });
    }

    // setup the adapter for the employees list view
    private void listViewAdapter(){
        EmployeeListViewAdapter employeeAdapter = new EmployeeListViewAdapter(view.getContext(), employees);
        employeeListView.setAdapter(employeeAdapter);

        Log.d(TAG, "listViewAdapter: adapter");
    }



}
