package com.example.a390project;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import com.google.firebase.database.ValueEventListener;

public class TaskPrePaintingActivity extends AppCompatActivity {

    private TextView mDescription;
    private ListView prepaintTasksListView;
    private FirebaseHelper firebaseHelper;
    private String taskId;
    private Button postCommentButton;
    private EditText newEmployeeCommentEdtText;
    private Button mComplete;
    private boolean backPressed[] = new boolean[1];
    private ValueEventListener valueEventListener;

    //check if user is manager from sharedpreferences
    private boolean isManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pre_painting);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        checkIfManager();
        backPressed[0] = false;
    }

    @Override
    protected void onStart(){
        super.onStart();
        setActionBar("Pre-Painting");

        setupUI();
        valueEventListener = firebaseHelper.populateSubTasks(taskId, this, backPressed);
        firebaseHelper.getEmployeeComments(this, taskId);
    }

    private void setupUI(){
        prepaintTasksListView = findViewById(R.id.prepaintTaskListView);
        mDescription = findViewById(R.id.prepaint_task_description);

        mDescription.setText(getIntent().getStringExtra("prepaintingDescription"));
        taskId = getIntent().getStringExtra("prepaintingTaskID");
        firebaseHelper = new FirebaseHelper();

        // comment function
        postCommentButton = findViewById(R.id.postCommentButton);
        newEmployeeCommentEdtText = findViewById(R.id.newCommentsEditText);

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = newEmployeeCommentEdtText.getText().toString();

                // check if the editText field is empty, if it is empty output error
                if (comment.isEmpty()){
                    newEmployeeCommentEdtText.setError("Field is empty");
                }
                // else save comment into firebase
                else {
                    firebaseHelper.postComment(taskId, comment);
                    newEmployeeCommentEdtText.getText().clear();
                    Toast.makeText(TaskPrePaintingActivity.this, "Comment Posted", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mComplete = findViewById(R.id.prepaintingCompletedButton);
        if(isManager) {
            mComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseHelper.allowCompleteTask(taskId, TaskPrePaintingActivity.this);
                }
            });
        }
        else {
            mComplete.setVisibility(View.GONE);
        }
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
    protected void onPause() {
        super.onPause();
        firebaseHelper.detatchPrepaintValueEventListener(taskId, valueEventListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed[0] = true;
    }
}
