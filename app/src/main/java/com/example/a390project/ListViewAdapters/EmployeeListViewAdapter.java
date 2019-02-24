package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.Model.Employee;
import com.example.a390project.R;

import java.util.List;

public class EmployeeListViewAdapter extends BaseAdapter {

    private static final String TAG = "EmployeeListViewAdapter";
    private Context context;
    private List<Employee> employees;

    // Employee layout components
    private TextView employeeName;

    public EmployeeListViewAdapter(Context context, List<Employee> employees){
        this.context = context;
        this.employees = employees;
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Object getItem(int position) {
        return employees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView: gotView");
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_item_employee, parent, false);
        }


        setupUI(convertView);

        employeeName.setText(employees.get(position).getEmployeeName());


        return convertView;
    }

    private void setupUI(View view){
        employeeName = view.findViewById(R.id.EmployeeNameTextView);
    }
}
