package com.example.a390project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.DialogFragments.GraphDialogFragment;
import com.example.a390project.ForegroundServices.BigOvenGraphForegroundService;
import com.example.a390project.ForegroundServices.GFSOvenGraphForegroundService;
import com.example.a390project.Model.Machine;

public class MachineActivity extends AppCompatActivity {

    public static final String TAG = "MachineActivity";

    //views
    private TextView mMachineTitle;
    private TextView mMachineStatus;
    private TextView mTemperature;
    private Button mStartTemperatureGraph;
    private Button mEndTemperatureGraph;

    //variables
    private String machineTitle;
    private boolean machineStatus;
    private float temperature;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
        //set the actionbar with heading of machineName
        setActionBar(getIntent().getStringExtra("machine_title"));

        prepareViews();

        final FirebaseHelper firebaseHelper = new FirebaseHelper();

        mStartTemperatureGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGraphDialogFragment();
            }
        });

        mEndTemperatureGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent serviceIntent;
                if (machineTitle.equals("Big_Oven")) {
                    serviceIntent = new Intent(getApplicationContext(), BigOvenGraphForegroundService.class);
                }
                else {
                    serviceIntent = new Intent(getApplicationContext(), GFSOvenGraphForegroundService.class);
                }
                firebaseHelper.stopGraphing(machineTitle);
                stopService(serviceIntent);
                Toast.makeText(MachineActivity.this, "Graphing Completed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGraphDialogFragment() {
        GraphDialogFragment dialog = new GraphDialogFragment(machineTitle, machineStatus);
        dialog.show(getSupportFragmentManager(), "Graphable Projects");
    }

    private void prepareViews() {
        mMachineTitle = findViewById(R.id.machine_title_activity);
        mMachineStatus = findViewById(R.id.machine_status_activity);
        mStartTemperatureGraph = findViewById(R.id.open_graphable_projects);
        mEndTemperatureGraph = findViewById(R.id.end_graph_graphable);
        mTemperature = findViewById(R.id.temperature_machine_activity);

        machineTitle = getIntent().getStringExtra("machine_title");
        machineStatus = getIntent().getBooleanExtra("machine_status", false);
        temperature =  getIntent().getFloatExtra("machine_temperature", 0);
        Log.d(TAG, "Machine Fetched: " + machineTitle + " " + machineStatus);

        Machine machine = new Machine(machineTitle, machineStatus);

        mMachineTitle.setText(machine.getMachineTitle());
        mMachineStatus.setText(machine.isMachineStatus() ? "On" : "Off");

        if (!(machineTitle.equals("Big_Oven") || machineTitle.equals("GFS_Oven"))) {
            mStartTemperatureGraph.setVisibility(View.GONE);
            mEndTemperatureGraph.setVisibility(View.GONE);
        }

        mTemperature.setText(Float.toString(temperature) + "°F");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
