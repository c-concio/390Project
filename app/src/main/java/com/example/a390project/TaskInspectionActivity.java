package com.example.a390project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class TaskInspectionActivity extends AppCompatActivity {

    // activity widgets
    EditText partCountedEditText;
    EditText partAcceptedEditText;
    EditText partRejectedEditText;
    Button completeButton;

    FirebaseHelper firebaseHelper;
    String inspectionTaskID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_inspection);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setActionBar();
        setupUI();
        firebaseHelper.setTaskInspectionActivityListener(inspectionTaskID, this);
    }

    private void setupUI(){
        // find the ids of the widgets
        partCountedEditText = findViewById(R.id.partCountedEditText);
        partAcceptedEditText = findViewById(R.id.partAcceptedEditText);
        partRejectedEditText = findViewById(R.id.partRejectedEditText);
        completeButton = findViewById(R.id.completeButton);

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
    public void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
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
