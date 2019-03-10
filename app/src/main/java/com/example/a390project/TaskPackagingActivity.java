package com.example.a390project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

// Todo: navigate up

public class TaskPackagingActivity extends AppCompatActivity {

    // activity widgets
    EditText descriptionEditText;
    EditText dateEditText;
    EditText employeeCommentEditText;
    Button startTimeButton;
    Button endTimeButton;
    FirebaseHelper firebaseHelper;
    TextView hoursTextView;

    String packagingTaskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_packaging);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupUI();

        firebaseHelper.setTaskPackagingActivityListener(packagingTaskID, this);
    }

    private void setupUI(){
        // find the ids of the widgets
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        employeeCommentEditText = findViewById(R.id.employeeCommentEditText);
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        hoursTextView = findViewById(R.id.hoursTextView);

        startTimeButton.setOnClickListener(startTimeOnClickListener);
        endTimeButton.setOnClickListener(endTimeOnClickListener);

        descriptionEditText.setFocusable(false);
        dateEditText.setFocusable(false);
        employeeCommentEditText.setFocusable(false);
        hoursTextView.setVisibility(View.GONE);


        // setup the firebaseHelper
        firebaseHelper = new FirebaseHelper();
        Intent intent = getIntent();
        packagingTaskID = intent.getStringExtra("packagingTaskID");
    }

    // onClickListener for the startTime button
    View.OnClickListener startTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // when the button is clicked, the current time will be saved to the database as the start time
            firebaseHelper.setStartTime(packagingTaskID);
            /*startTimeButton.setEnabled(false);
            endTimeButton.setEnabled(true);*/
        }
    };

    // onClickListener for the endTime button
    View.OnClickListener endTimeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // when the button is clicked, the current time will be saved to the database as the end time
            firebaseHelper.setEndTime(packagingTaskID);
            /*endTimeButton.setEnabled(false);
            startTimeButton.setEnabled(true);*/
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        firebaseHelper.detatchTaskPackagingActivityListener(packagingTaskID);
    }
}
