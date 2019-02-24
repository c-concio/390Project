package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.a390project.Employee;
import com.example.a390project.ListViewAdapters.EmployeeListViewAdapter;
import com.example.a390project.R;

import java.util.ArrayList;
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
        employees = populateEmployees();

        listViewAdapter();
    }

    private List<Employee> populateEmployees(){
        List<Employee> sampleEmployee = new ArrayList<>();

        for(int i = 0; i < 100; i++){
            sampleEmployee.add(new Employee(i, "Employee " + i));
        }



        return sampleEmployee;
    }

    // setup the components in the layout
    private void setupUI(){
        // find the views
        employeeListView = view.findViewById(R.id.employeesListView);
    }

    // setup the adapter for the employees list view
    private void listViewAdapter(){
        EmployeeListViewAdapter employeeAdapter = new EmployeeListViewAdapter(view.getContext(), employees);
        employeeListView.setAdapter(employeeAdapter);

        Log.d(TAG, "listViewAdapter: adapter");
    }



}
