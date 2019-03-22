package com.example.a390project.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.ListViewAdapters.EmployeeTasksListViewAdapter;
import com.example.a390project.Model.Task;
import com.example.a390project.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class EmployeeTasksFragment extends Fragment {

    public static final String TAG = "EmployeeTasksFrag";

    // activity components
    ListView assignedTasksListView;
    ListView completedTasksListView;
    TextView noWorkingTasksTextView;
    TextView noCompletedTasksTextView;

    // variables
    List<Task> assignedTasks = new ArrayList<>();
    List<Task> completedTasks = new ArrayList<>();
    String employeeID;
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    public EmployeeTasksFragment (String employeeID) {
        this.employeeID = employeeID;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.employee_tasks_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // setup the adapters and make the height of the list views dynamic

        setupUI(view);
    }

    private void setupUI(View view){
        assignedTasksListView = view.findViewById(R.id.workingTasksListView);
        completedTasksListView = view.findViewById(R.id.completedTasksListView);
        noWorkingTasksTextView = view.findViewById(R.id.noWorkingTasksTextView);
        noCompletedTasksTextView = view.findViewById(R.id.noCompletedTasksTextView);

        noWorkingTasksTextView.setVisibility(View.GONE);
        noCompletedTasksTextView.setVisibility(View.GONE);

        FirebaseHelper.setEmployeeTasksListViewHeightBasedOnChildren(assignedTasksListView);
        FirebaseHelper.setEmployeeTasksListViewHeightBasedOnChildren(completedTasksListView);

        firebaseHelper.setWorkingTasksValueListener(employeeID, noWorkingTasksTextView, getActivity(), assignedTasksListView);
        firebaseHelper.setCompletedTasksValueEventListener(employeeID, noCompletedTasksTextView, getActivity());

        assignedTasksListViewAdapter();
        completedTasksListViewAdapter();

    }

    private void assignedTasksListViewAdapter(){
        EmployeeTasksListViewAdapter taskAdapter = new EmployeeTasksListViewAdapter(getContext(), assignedTasks);
        assignedTasksListView.setAdapter(taskAdapter);
    }

    private void completedTasksListViewAdapter(){
        EmployeeTasksListViewAdapter taskAdapter = new EmployeeTasksListViewAdapter(getContext(), completedTasks);
        completedTasksListView.setAdapter(taskAdapter);
    }
}
