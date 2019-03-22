package com.example.a390project;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskPrePaintingActivity extends AppCompatActivity {

    private TextView mDescription;
    private ListView prepaintTasksListView;
    private FirebaseHelper firebaseHelper;
    private String taskId;
    private Button postCommentButton;
    private EditText newEmployeeCommentEdtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_pre_painting);
    }

    @Override
    protected void onStart(){
        super.onStart();
        setActionBar("Pre-Painting");

        setupUI();

        firebaseHelper.populateSubTasks(taskId, this);
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
        newEmployeeCommentEdtText = findViewById(R.id.newEmployeeCommentEditText);

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextValue = newEmployeeCommentEdtText.getText().toString();

                // check if the editText field is empty, if it is empty output error
                if (editTextValue.isEmpty()){
                    newEmployeeCommentEdtText.setError("Field is empty");
                }
                // else save comment into firebase
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
