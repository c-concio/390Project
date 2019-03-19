package com.example.a390project;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        setupUI();
        firebaseHelper.setTaskInspectionActivityListener(inspectionTaskID, this);


        //Text changed listener
        partCountedEditText.addTextChangedListener(new TextWatcher() {
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
                    partCountedEditText.setSelection(s.length());   //Places cursor at the end
                }
            }
        });

        partAcceptedEditText.addTextChangedListener(new TextWatcher() {
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
                    partAcceptedEditText.setSelection(s.length());   //Places cursor at the end
                }
            }
        });

        partRejectedEditText.addTextChangedListener(new TextWatcher() {
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
                    partRejectedEditText.setSelection(s.length());   //Places cursor at the end
                }
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
