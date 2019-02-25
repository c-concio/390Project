package com.example.a390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.a390project.ListViewAdapters.EmployeeTasksListViewAdapter;
import com.example.a390project.Model.EmployeeTasks;

import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    // activity components
    ListView assignedTasksListView;
    ListView completedTasksListView;

    // variables
    List<EmployeeTasks> assignedTasks;
    List<EmployeeTasks> completedTasks;
    int employeeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        // setup the components
        setupUI();

        // dummy tasks
        DummyDatabase db = new DummyDatabase();

        assignedTasks = db.generateTasks(employeeID - 1);
        completedTasks = db.generateTasks(employeeID + 2);

        assignedTasksListViewAdapter();
        completedTasksListViewAdapter();

        ListUtils.setDynamicHeight(assignedTasksListView);
        ListUtils.setDynamicHeight(completedTasksListView);
    }

    private void setupUI(){
        assignedTasksListView = findViewById(R.id.assignedTasksListView);
        completedTasksListView = findViewById(R.id.completedTasksListView);

        // get intent to get extra
        Intent intent = getIntent();
        employeeID = intent.getIntExtra("employeeID", -1);
    }

    private void assignedTasksListViewAdapter(){
        EmployeeTasksListViewAdapter taskAdapter = new EmployeeTasksListViewAdapter(this, assignedTasks);
        assignedTasksListView.setAdapter(taskAdapter);
    }

    private void completedTasksListViewAdapter(){
        EmployeeTasksListViewAdapter taskAdapter = new EmployeeTasksListViewAdapter(this, completedTasks);
        completedTasksListView.setAdapter(taskAdapter);
    }

    // create a list utility class to dynamically change the height of the listView
    // reference: https://stackoverflow.com/questions/17693578/android-how-to-display-2-listviews-in-one-activity-one-after-the-other
    public static class ListUtils{
        private static void setDynamicHeight(ListView listView){
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null){
                return;
            }

            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);

            for(int i = 0; i < listAdapter.getCount(); i++){
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = height + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

}
