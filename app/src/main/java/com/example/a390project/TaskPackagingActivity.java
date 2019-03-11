package com.example.a390project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

// Todo: navigate up
// Todo: input set firebase

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
        setActionBar("Packaging");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

        /*descriptionEditText.setFocusable(false);
        dateEditText.setFocusable(false);
        employeeCommentEditText.setFocusable(false);*/
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
        firebaseHelper.detachTaskPackagingActivityListener(packagingTaskID);
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
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
