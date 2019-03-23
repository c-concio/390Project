package com.example.a390project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TaskInspectionActivity extends AppCompatActivity {

    private static final String TAG = "TaskInspectionActivity";

    // activity widgets
    EditText mCounted;
    EditText mAccepted;
    EditText mRejected;
    Button mStartTime;
    Button mEndTime;
    Button mCompleteTime;

    FirebaseHelper firebaseHelper;
    String inspectionTaskID;
    String inspectionType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_inspection);
        setupUI();
        firebaseHelper.setTaskInspectionActivityListener(inspectionTaskID, TaskInspectionActivity.this);
        firebaseHelper.setStartTimeEndTimeButtons(mStartTime,mEndTime,inspectionTaskID);


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

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                inspectionType = intent.getStringExtra("taskType");
                Log.d(TAG, "onClick: " + inspectionType);
                firebaseHelper.checkIfCanStart(inspectionTaskID, getApplicationContext(), inspectionType, TaskInspectionActivity.this,mStartTime,mEndTime);
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.checkIfCanEnd(inspectionTaskID, getApplicationContext());
            }
        });

        mCompleteTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.completeTask(inspectionTaskID);
            }
        });


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
        mCompleteTime = findViewById(R.id.completeButton);
        mStartTime = findViewById(R.id.startTimeButton_inspection);
        mEndTime = findViewById(R.id.endTimeButton_inspection);

        firebaseHelper = new FirebaseHelper();
        Intent intent = getIntent();
        inspectionTaskID = intent.getStringExtra("inspectionTaskID");

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
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
