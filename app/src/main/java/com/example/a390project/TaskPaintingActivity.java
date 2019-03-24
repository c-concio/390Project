package com.example.a390project;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private Button saveButton;

    // views for liquid paint
    LinearLayout liquidLinearLayout;
    EditText viscosityEditText;
    EditText tipSizeEditText;
    EditText pressureLiquidEditText;

    // views for powder paint
    LinearLayout powderLinearLayout;
    EditText amountEditText;
    EditText spreadEditText;
    Switch reCoatSwitch;
    EditText pressurePowderEditText;

    private String employeeComment;
    private String paintingTaskID;

    private static final String TAG = "TaskPaintingActivity";

    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    DatabaseReference dbRef;

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
        mComment = findViewById(R.id.newCommentsEditText);
        mPostCommentButton = findViewById(R.id.postCommentButton);
        employeeCommentsListView = findViewById(R.id.employeeCommentsListView);

        paintingTaskID = getIntent().getStringExtra("paintingTaskID");
        firebaseHelper.setStartTimeEndTimeButtons(mStartTime,mEndTime,paintingTaskID);
        firebaseHelper.setPaintingValues(mPaintCode, mBakeTemp, mBakeTime, mDescription, mPaintDescription, paintingTaskID, this);

        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.checkIfCanStart(paintingTaskID, getApplicationContext(), "Painting", TaskPaintingActivity.this,mStartTime,mEndTime);
                Toast.makeText(TaskPaintingActivity.this, "Task Started!" , Toast.LENGTH_SHORT).show();
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.checkIfCanEnd(paintingTaskID, getApplicationContext());
                Toast.makeText(TaskPaintingActivity.this, "Task Ended!" , Toast.LENGTH_SHORT).show();
            }
        });

        mCompletedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.completeTask(paintingTaskID);
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

        setupEditTexts();

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

    private void setupEditTexts(){
        // liquid
        liquidLinearLayout = findViewById(R.id.liquidLinearLayout);
        viscosityEditText = findViewById(R.id.viscosityEditText);
        tipSizeEditText = findViewById(R.id.tipSizeEditText);
        pressureLiquidEditText = findViewById(R.id.pressureLiquidEditText);

        // views for powder paint
        powderLinearLayout = findViewById(R.id.powderLinearLayout);
        amountEditText = findViewById(R.id.amountEditText);
        spreadEditText = findViewById(R.id.spreadEditText);
        reCoatSwitch = findViewById(R.id.reCoatSwitch);
        pressurePowderEditText = findViewById(R.id.pressurePowderEditText);

        // button
        saveButton = findViewById(R.id.saveButton);

        dbRef = FirebaseDatabase.getInstance().getReference().child("tasks").child(paintingTaskID).child("paintType");

        reCoatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                firebaseHelper.setReCoat(paintingTaskID, reCoatSwitch.isChecked());
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if liquid layout is gone, then task is powder
                if (liquidLinearLayout.getVisibility() == View.GONE){
                    if (!amountEditText.getText().toString().equals(""))
                        firebaseHelper.setAmount(paintingTaskID, Long.parseLong(amountEditText.getText().toString()));
                    if (!spreadEditText.getText().toString().equals(""))
                        firebaseHelper.setSpread(paintingTaskID, Long.parseLong(spreadEditText.getText().toString()));
                    if (!pressurePowderEditText.getText().toString().equals(""))
                        firebaseHelper.setPressure(paintingTaskID, Long.parseLong(pressurePowderEditText.getText().toString()));
                }
                if (powderLinearLayout.getVisibility() == View.GONE){
                    if (!viscosityEditText.getText().toString().equals(""))
                        firebaseHelper.setViscosity(paintingTaskID, Long.parseLong(viscosityEditText.getText().toString()));
                    if (!tipSizeEditText.getText().toString().equals(""))
                        firebaseHelper.setTipSize(paintingTaskID, Long.parseLong(tipSizeEditText.getText().toString()));
                    if (!pressureLiquidEditText.getText().toString().equals(""))
                        firebaseHelper.setPressure(paintingTaskID, Long.parseLong(pressureLiquidEditText.getText().toString()));
                }
            }
        });


    }

}
