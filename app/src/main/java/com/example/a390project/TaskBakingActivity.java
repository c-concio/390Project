package com.example.a390project;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class TaskBakingActivity extends AppCompatActivity {



    String bakingTaskID;

    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_baking);
    }

    @Override
    protected void onStart(){
        super.onStart();
        setupUI();
        setActionBar("Baking");
        firebaseHelper.setBakingValueEventListener(bakingTaskID, this);
    }

    private void setupUI(){


        firebaseHelper = new FirebaseHelper();
        bakingTaskID = getIntent().getStringExtra("bakingTaskID");
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
