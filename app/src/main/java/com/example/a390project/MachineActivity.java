package com.example.a390project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.Model.Machine;

public class MachineActivity extends AppCompatActivity {

    public static final String TAG = "MachineActivity";

    //views
    private TextView mMachineTitle;
    private TextView mMachineID;
    private TextView mMachineStatus;
    private Switch mMachineNotifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);
        //set the actionbar with heading of machineName
        setActionBar(getIntent().getStringExtra("machine_title"));

        prepareViews();

        //Controller will only need to send machineID from MachineListViewAdapter to machineActivity,
        // which will get the entire Machine Object from Firebase
        String machineID = getIntent().getStringExtra("machine_ID");
        String machineTitle = getIntent().getStringExtra("machine_title");
        boolean machineStatus = getIntent().getBooleanExtra("machine_status",false);
        Log.d(TAG, "Machine Fetched: " + machineID + " " + machineTitle + " " + machineStatus);

        Machine machine = new Machine (machineID, machineTitle, "-", machineStatus);

        mMachineTitle.setText(machine.getMachineTitle());
        mMachineID.setText(machine.getMachineID());
        mMachineStatus.setText(machine.isMachineStatus() ? "On":"Off");

        //create controller to enable/disable notifications
        mMachineNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "Notifications Checked ON");
                    Toast.makeText(MachineActivity.this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "Notifications Checked OFF");
                    Toast.makeText(MachineActivity.this, "Notifications Disabled", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void prepareViews() {
        mMachineTitle = findViewById(R.id.machine_title_activity);
        mMachineID = findViewById(R.id.machine_id_activity);
        mMachineStatus = findViewById(R.id.machine_status_activity);
        mMachineNotifications = findViewById(R.id.machine_notifications_acitivty);
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
