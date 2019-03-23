package com.example.a390project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.DialogFragments.GraphDialogFragment;
import com.example.a390project.Model.Machine;

public class MachineActivity extends AppCompatActivity {

    public static final String TAG = "MachineActivity";

    //views
    private TextView mMachineTitle;
    private TextView mMachineStatus;
    private Button mStartTemperatureGraph;
    private Button mEndTemperatureGraph;

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

            }
        });

    }

    private void openGraphDialogFragment() {
        GraphDialogFragment dialog = new GraphDialogFragment();
        dialog.show(getSupportFragmentManager(), "Graphable Projects");
    }

    private void prepareViews() {
        mMachineTitle = findViewById(R.id.machine_title_activity);
        mMachineStatus = findViewById(R.id.machine_status_activity);
        mStartTemperatureGraph = findViewById(R.id.open_graphable_projects);
        mEndTemperatureGraph = findViewById(R.id.end_graph_graphable);

        String machineTitle = getIntent().getStringExtra("machine_title");
        boolean machineStatus = getIntent().getBooleanExtra("machine_status", false);
        Log.d(TAG, "Machine Fetched: " + machineTitle + " " + machineStatus);

        Machine machine = new Machine(machineTitle, machineStatus);

        mMachineTitle.setText(machine.getMachineTitle());
        mMachineStatus.setText(machine.isMachineStatus() ? "On" : "Off");

        if (!(machineTitle.equals("Big_Oven") || machineTitle.equals("GFS_Oven"))) {
            mStartTemperatureGraph.setVisibility(View.GONE);
            mEndTemperatureGraph.setVisibility(View.GONE);
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
