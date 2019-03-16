package com.example.a390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.a390project.ListViewAdapters.EmployeeTasksListViewAdapter;
import com.example.a390project.Model.EmployeeTasks;
import com.example.a390project.Model.Task;

import java.util.ArrayList;
import java.util.List;

// Todo: Create Assigned tasks in Firebase
// Todo: Create Completed tasks in Firebase

public class EmployeeActivity extends AppCompatActivity{

    // activity components
    ListView assignedTasksListView;
    ListView completedTasksListView;

    // variables
    List<Task> assignedTasks = new ArrayList<>();
    List<Task> completedTasks = new ArrayList<>();
    String employeeID;

    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        // provide up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // setup the components
        setupUI();

        // setup the adapters and make the height of the list views dynamic
        assignedTasksListViewAdapter();
        completedTasksListViewAdapter();

        ListUtils.setDynamicHeight(assignedTasksListView);
        ListUtils.setDynamicHeight(completedTasksListView);

        firebaseHelper.setAssignedTasksValueListener(employeeID, this);
        firebaseHelper.setCompletedTasksValueEventListener(employeeID, this);

    }

    private void setupUI(){
        assignedTasksListView = findViewById(R.id.assignedTasksListView);
        completedTasksListView = findViewById(R.id.completedTasksListView);

        // get intent to get extra
        Intent intent = getIntent();
        employeeID = intent.getStringExtra("employeeID");

        firebaseHelper = new FirebaseHelper();

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
    private static class ListUtils{
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

    //up navigation

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
