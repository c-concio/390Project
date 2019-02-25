package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.Model.EmployeeTasks;
import com.example.a390project.R;
import com.google.android.gms.tasks.Tasks;

import java.util.List;

public class EmployeeTasksListViewAdapter extends BaseAdapter {

    // views
    private TextView taskTextView;

    // UI components
    private List<EmployeeTasks> tasks;
    private Context context;

    public EmployeeTasksListViewAdapter(Context context, List<EmployeeTasks> tasks){
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_item_employee_tasks, parent, false);
        }


        setupUI(convertView);

        taskTextView.setText(tasks.get(position).getTaskTitle());

        return convertView;
    }

    private void setupUI(View view){
        taskTextView = view.findViewById(R.id.taskTextView);
    }
}
