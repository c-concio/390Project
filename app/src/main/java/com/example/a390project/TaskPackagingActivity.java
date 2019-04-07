package com.example.a390project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Todo: input set firebase

public class TaskPackagingActivity extends AppCompatActivity {

    // activity widgets
    TextView descriptionTextView;
    EditText employeeCommentEditText;
    Button startTimeButton;
    Button endTimeButton;
    Button completeButton;
    Button postCommentButton;
    FirebaseHelper firebaseHelper;
    TextView mTimeElapsed;
    private boolean backPressed[] = new boolean[1];

    String packagingTaskID;

    //check if user is manager from sharedpreferences
    private boolean isManager;

    private static final String TAG = "TaskPackagingActivity";


    // ------------------------- temporary ----------------------------
    Button addMaterialButton;
    Spinner materialSpinner;

    ValueEventListener materialSpinnerValueEventListener;
    ValueEventListener packagingValueEventListener;
    // ------------------------- temporary ----------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_packaging);

        checkIfManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActionBar("Packaging");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setupUI();
        packagingValueEventListener = firebaseHelper.setTaskPackagingActivityListener(packagingTaskID, this);
        firebaseHelper.setStartTimeEndTimeButtons(startTimeButton,endTimeButton,packagingTaskID);
        firebaseHelper.getEmployeeComments(this, packagingTaskID);
        materialSpinnerValueEventListener = firebaseHelper.populateMaterialSpinner(this);

    }

    private void setupUI(){
        // find the ids of the widgets
        descriptionTextView = findViewById(R.id.descriptionTextView);
        employeeCommentEditText = findViewById(R.id.employeeCommentEditText);
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        completeButton = findViewById(R.id.completed_packaging_task);
        postCommentButton = findViewById(R.id.postCommentButton);
        mTimeElapsed = findViewById(R.id.elapsed_time_since_pressed_start_time_packaging);
        mTimeElapsed.setVisibility(View.GONE);

        startTimeButton.setOnClickListener(startTimeOnClickListener);
        endTimeButton.setOnClickListener(endTimeOnClickListener);
        if (isManager)
            completeButton.setOnClickListener(completeOnClickListener);
        else
            completeButton.setVisibility(View.GONE);
        postCommentButton.setOnClickListener(postCommentOnClickListener);

        materialSpinner = findViewById(R.id.materialSpinner);
        addMaterialButton = findViewById(R.id.addMaterialButton);
        addMaterialButton.setOnClickListener(addMaterialOnClickListener);



        // setup the firebaseHelper
        firebaseHelper = new FirebaseHelper();
        Intent intent = getIntent();
        packagingTaskID = intent.getStringExtra("packagingTaskID");


        //check if workblock already running for this task and display the timer
        backPressed[0] = false;
        firebaseHelper.checkIfTaskStartedAlready(packagingTaskID, mTimeElapsed, TaskPackagingActivity.this, backPressed, null, "Packaging");
    }

    View.OnClickListener addMaterialOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String selectedMaterial = materialSpinner.getSelectedItem().toString();
            firebaseHelper.addMaterialUsed(packagingTaskID, selectedMaterial);
        }
    };


    // onClickListener for the startTime button
    View.OnClickListener startTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseHelper.checkIfCanStart(packagingTaskID, getApplicationContext(), "Packaging", TaskPackagingActivity.this,startTimeButton,endTimeButton, mTimeElapsed, backPressed, null);
        }
    };

    // onClickListener for the endTime button
    View.OnClickListener endTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseHelper.checkIfCanEnd(packagingTaskID, getApplicationContext(), mTimeElapsed, null);
        }
    };

    /*
        -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    */

    // onClickListener for the complete button
    View.OnClickListener completeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            firebaseHelper.completeTask(packagingTaskID);
        }
    };

    View.OnClickListener postCommentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String commentString = employeeCommentEditText.getText().toString();
            if (commentString.isEmpty()){
                employeeCommentEditText.setError("Field is empty");
            }
            else{
                firebaseHelper.postComment(packagingTaskID, commentString);
                employeeCommentEditText.getText().clear();
                Toast.makeText(TaskPackagingActivity.this, "Comment posted", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference().removeEventListener(packagingValueEventListener);
        // detatch materialValueEventListener
        FirebaseDatabase.getInstance().getReference().child("inventory").child("material").removeEventListener(materialSpinnerValueEventListener);
    }

    //custom heading and back button
    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mTimeElapsed.setVisibility(View.GONE);
                backPressed[0] = true;
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkIfManager() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String manager = preferences.getString("isManager",null);
        if (manager.equals("true")) {
            isManager = true;
        }
        else {
            isManager = false;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mTimeElapsed.setVisibility(View.GONE);
        backPressed[0] = true;
    }
}
