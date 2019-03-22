package com.example.a390project;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskPaintingActivity extends AppCompatActivity {

    private TextView mPaintCode;
    private TextView mPaintDescription;
    private TextView mBakeTemp;
    private TextView mBakeTime;
    private TextView mDescription;
    private Button mStartTime;
    private Button mEndTime;
    private Button mCompletedTime;
    private Button mPostCommentButton;
    private EditText mComment;
    private ListView employeeCommentsListView;

    private String employeeComment;
    private String paintingTaskID;

    private static final String TAG = "TaskPaintingActivity";

    private FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_painting);

        mPaintCode = findViewById(R.id.paint_code_painting_task);
        mPaintDescription = findViewById(R.id.paint_description_painting_task);
        mBakeTemp = findViewById(R.id.baking_temperature_painting_task);
        mBakeTime = findViewById(R.id.baking_time_painting_task);
        mDescription = findViewById(R.id.task_description_painting_task);
        mStartTime = findViewById(R.id.start_time_painting_task);
        mEndTime = findViewById(R.id.end_time_painting_task);
        mCompletedTime = findViewById(R.id.completed_painting_task);
        mComment = findViewById(R.id.newEmployeeCommentEditText);
        mPostCommentButton = findViewById(R.id.postCommentButton);
        employeeCommentsListView = findViewById(R.id.employeeCommentsListView);


        FirebaseHelper firebaseHelper = new FirebaseHelper();

        paintingTaskID = getIntent().getStringExtra("paintingTaskID");
        firebaseHelper.setPaintingValues(mPaintCode, mBakeTemp, mBakeTime, mDescription, mPaintDescription, paintingTaskID);

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskPaintingActivity.this, "Task Started!" , Toast.LENGTH_SHORT).show();
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskPaintingActivity.this, "Task Ended!" , Toast.LENGTH_SHORT).show();
            }
        });

        mCompletedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskPaintingActivity.this, "Painting Completed!", Toast.LENGTH_SHORT).show();
                employeeComment = mComment.getText().toString().trim();
            }
        });

        setActionBar("Painting");

        Log.d(TAG, "onCreate: getting all comments");
        firebaseHelper.getEmployeeComments(this, paintingTaskID);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // if post button is clicked, checks if the editText is empty or not. If it is empty, get the string in the editText and save it to firebase
        mPostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentString = mComment.getText().toString();
                if (commentString.isEmpty()){
                    mComment.setError("Field is empty");
                }
                else{
                    firebaseHelper.postComment(paintingTaskID, commentString);
                    mComment.getText().clear();
                    Toast.makeText(TaskPaintingActivity.this, "Comment posted", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
