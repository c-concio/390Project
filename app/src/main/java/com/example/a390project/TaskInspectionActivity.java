package com.example.a390project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TaskInspectionActivity extends AppCompatActivity {

    private static final String TAG = "TaskInspectionActivity";

    // activity widgets
    EditText mCounted;
    EditText mAccepted;
    EditText mRejected;
    EditText mEmployeeCommentEditText;
    Button mStartTime;
    Button mEndTime;
    Button mCompleteTime;
    Button mPostCommentButton;
    TextView mTimeElapsed;
    private boolean backPressed[] = new boolean[1];

    FirebaseHelper firebaseHelper;
    String inspectionTaskID;
    String inspectionType;

    //check if user is manager from sharedpreferences
    private boolean isManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_inspection);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        inspectionType = intent.getStringExtra("taskType");
        inspectionTaskID = intent.getStringExtra("inspectionTaskID");
        setupUI();
        firebaseHelper.setTaskInspectionActivityListener(inspectionTaskID, TaskInspectionActivity.this);
        firebaseHelper.setStartTimeEndTimeButtons(mStartTime,mEndTime,inspectionTaskID);



        checkIfManager();

        //Text changed listener
        mCounted.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String partCounted = s.toString();
                if(!partCounted.equals("")) {
                    firebaseHelper.changeInspectionCounted(inspectionTaskID, Integer.parseInt(partCounted));
                    mCounted.setSelection(s.length());   //Places cursor at the end
                }
            }
        });

        mAccepted.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String partAccepted = s.toString();
                if(!partAccepted.equals("")) {
                    firebaseHelper.changeInspectionAccepted(inspectionTaskID, Integer.parseInt(partAccepted));
                    mAccepted.setSelection(s.length());   //Places cursor at the end
                }
            }
        });

        mRejected.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String partRejected = s.toString();
                if(!partRejected.equals("")) {
                    firebaseHelper.changeInspectionRejected(inspectionTaskID, Integer.parseInt(partRejected));
                    mRejected.setSelection(s.length());   //Places cursor at the end
                }
            }
        });

        /*
            -------------------------- Start and End time buttons creating workblocks, updating persistent notification and updating mTimeElapsed textview-------------------------
         */
        backPressed[0] = false;
        firebaseHelper.checkIfTaskStartedAlready(inspectionTaskID, mTimeElapsed, TaskInspectionActivity.this, backPressed, null, inspectionType);

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + inspectionType);
                firebaseHelper.checkIfCanStart(inspectionTaskID, getApplicationContext(), inspectionType, TaskInspectionActivity.this,mStartTime,mEndTime, mTimeElapsed,backPressed, null);
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.checkIfCanEnd(inspectionTaskID, getApplicationContext(), mTimeElapsed, null);
            }
        });

        /*
        -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

        if (isManager) {
            mCompleteTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseHelper.allowCompleteTask(inspectionTaskID, TaskInspectionActivity.this);
                }
            });
        }
        else {
            mCompleteTime.setVisibility(View.GONE);
        }

        mPostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentString = mEmployeeCommentEditText.getText().toString();
                if (commentString.isEmpty()){
                    mEmployeeCommentEditText.setError("Field is empty");
                }
                else{
                    firebaseHelper.postComment(inspectionTaskID, commentString);
                    mEmployeeCommentEditText.getText().clear();
                    Toast.makeText(TaskInspectionActivity.this, "Comment posted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseHelper.getEmployeeComments(this, inspectionTaskID);


    }

    @Override
    protected void onStart() {
        super.onStart();
        setActionBar("Inspection");

    }



    private void setupUI(){
        // find the ids of the widgets
        mCounted = findViewById(R.id.partCountedEditText);
        mAccepted = findViewById(R.id.partAcceptedEditText);
        mRejected = findViewById(R.id.partRejectedEditText);
        mEmployeeCommentEditText = findViewById(R.id.newCommentsEditText);
        mCompleteTime = findViewById(R.id.completeButton);
        mStartTime = findViewById(R.id.startTimeButton_inspection);
        mEndTime = findViewById(R.id.endTimeButton_inspection);
        mPostCommentButton = findViewById(R.id.postCommentButton);
        mTimeElapsed = findViewById(R.id.elapsed_time_since_pressed_start_time);
        mTimeElapsed.setVisibility(View.GONE);

        firebaseHelper = new FirebaseHelper();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseHelper.detachTaskInspectionActivityListener(inspectionTaskID);
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
