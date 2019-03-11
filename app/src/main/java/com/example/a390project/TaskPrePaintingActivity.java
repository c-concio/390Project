package com.example.a390project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class TaskPrePaintingActivity extends AppCompatActivity {

    ListView prepaintTasksListView;
    FirebaseHelper firebaseHelper;
    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pre_painting);
    }

    @Override
    protected void onStart(){
        super.onStart();

        setupUI();

        firebaseHelper.setPrepaintTaskValueListener(taskId, this);
    }

    private void setupUI(){
        prepaintTasksListView = findViewById(R.id.prepaintTaskListView);
        taskId = getIntent().getStringExtra("prepaintTaskID");
        firebaseHelper = new FirebaseHelper();
    }
}
