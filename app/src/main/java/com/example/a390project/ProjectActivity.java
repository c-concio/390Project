package com.example.a390project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a390project.DialogFragments.CreateMachineDialogFragment;
import com.example.a390project.DialogFragments.CreateTaskDialogFragment;
import com.example.a390project.Model.Task;

import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private static final String TAG = "ProjectActivity";

    //views
    private FloatingActionButton mFabOpenTaskDialogFragment;

    //variables
    private String projectPO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        prepareActivity();
        projectPO = getIntent().getStringExtra("projectPO");
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.populateTasks(projectPO, ProjectActivity.this);
    }

    private void prepareActivity() {
        upNavigation();

        mFabOpenTaskDialogFragment = findViewById(R.id.fab_open_dialog_fragment_task);
        mFabOpenTaskDialogFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateTaskDialogFragment();
            }
        });
    }

    private void startCreateTaskDialogFragment() {
        CreateTaskDialogFragment dialog = new CreateTaskDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Task");
    }

    private void upNavigation() {
        // provide up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("ProjectTitle"));
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

    public String getProjectPO() {
        return projectPO;
    }
}
