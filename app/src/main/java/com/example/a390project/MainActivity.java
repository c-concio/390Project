package com.example.a390project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.a390project.DialogFragments.CreateControlDeviceDialogFragment;
import com.example.a390project.DialogFragments.CreateEmployeeDialogFragment;
import com.example.a390project.DialogFragments.CreateInventoryDialogFragment;
import com.example.a390project.DialogFragments.CreateMachineDialogFragment;
import com.example.a390project.DialogFragments.CreateProjectDialogFragment;
import com.example.a390project.Fragments.ControlDeviceFragment;
import com.example.a390project.Fragments.EmployeeFragment;
import com.example.a390project.Fragments.InventoryFragment;
import com.example.a390project.Fragments.MachineFragment;
import com.example.a390project.Fragments.ProjectFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private DatabaseReference  rootRef = FirebaseDatabase.getInstance().getReference();
    private String uId = FirebaseAuth.getInstance().getUid();
    private boolean isManager;
    private ProgressBar mProgressbar;

    //This is our tab-layout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    //views
    FloatingActionButton mFabOpenDialogFragmentMachine;
    FloatingActionButton mFabOpenDialogFragmentControlDevice;
    FloatingActionButton mFabOpenDialogFragmentProject;
    FloatingActionButton mFabOpenDialogFragmentInventory;
    //firebase auth
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        //firebase authentication
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.LogOutbtnn:{
                mAuth.signOut();
                finish();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
            case R.id.ProfileBtnn:{
                Intent profactivity = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profactivity);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Log.d(TAG,"No user logged in or registered");
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }
        else {
            checkIfManager();
            Toast.makeText(this,"Welcome back",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfManager() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        rootRef.child("users").child(uId).child("manager").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(boolean.class)) {
                    editor.remove("isManager");
                    editor.putString("isManager","true");
                    isManager = true;
                }
                else {
                    editor.remove("isManager");
                    editor.putString("isManager", "false");
                    isManager = false;
                }
                editor.apply();
                final String manager = preferences.getString("isManager",null);
                Log.d(TAG, "Fetched isManager from Firebase: " + isManager);

                if (manager.equals("true")) {
                    isManager = true;
                    prepareActivity(isManager);
                    Log.d(TAG, "Fetched isManager from SharedPreferences: " + isManager);
                }
                else if (manager.equals("false")){
                    isManager = false;
                    prepareActivity(isManager);
                    Log.d(TAG, "Fetched isManager from SharedPreferences: " + isManager);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void prepareActivity(final boolean isManager) {
        //instantiate fragment views
        mProgressbar = findViewById(R.id.progress_bar_main_activity);
        mProgressbar.setVisibility(View.VISIBLE);

        mFabOpenDialogFragmentProject = findViewById(R.id.fab_open_dialog_fragment_project);
        mFabOpenDialogFragmentProject.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startCreateProjectDialogFragment();
            }
        });
        mFabOpenDialogFragmentControlDevice = findViewById(R.id.fab_open_dialog_fragment_control_device);
        mFabOpenDialogFragmentControlDevice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startCreateControlDeviceDialogFragment();
            }
        });
        mFabOpenDialogFragmentMachine = findViewById(R.id.fab_open_dialog_fragment_machine);
        mFabOpenDialogFragmentMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateMachineDialogFragment();
            }
        });
        mFabOpenDialogFragmentInventory = findViewById(R.id.fab_open_dialog_fragment_inventory);
        mFabOpenDialogFragmentInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateInventoryDialogFragment();
            }
        });
        if (isManager)
            mFabOpenDialogFragmentProject.show();
        else
            mFabOpenDialogFragmentProject.hide();
        mFabOpenDialogFragmentControlDevice.hide();
        mFabOpenDialogFragmentMachine.hide();
        mFabOpenDialogFragmentInventory.hide();

        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5); // <---- .setOffscreenPageLimit controls the max amount of tabs

        //Initializing the tablayout
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isManager) {
                    animateFab(position);
                }
                else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //To add a fragment to a viewPager;
        // 1.create an instance of the fragment
        // 2.add the created instance to the adapter

        //fragement1 = new Fragment1();
        //fragement2 = new Fragment2();

        //adapter.addFragment(fragment1,"FRAGMENT_1_TITLE");
        //adapter.addFragment(fragment2,"FRAGMENT_2_TITLE");

        // ------------------ PROJECT FRAGMENT ------------------------
        ProjectFragment projectFragment = new ProjectFragment(mProgressbar);
        adapter.addFragment(projectFragment, "PROJECTS");

        if (isManager) {
            // ------------------ EMPLOYEE FRAGMENT ------------------
            EmployeeFragment employeeFragment = new EmployeeFragment();
            adapter.addFragment(employeeFragment, "EMPLOYEES");

            // ------------------ CONTROL DEVICE FRAGMENT ------------------
            ControlDeviceFragment controlDeviceFragment = new ControlDeviceFragment();
            adapter.addFragment(controlDeviceFragment, "DEVICES");
        }

        // ------------------ MACHINE FRAGMENT ------------------
        MachineFragment machineFragment = new MachineFragment();
        adapter.addFragment(machineFragment,"MACHINES");

        InventoryFragment inventoryFragment = new InventoryFragment();
        adapter.addFragment(inventoryFragment, "INVENTORY");

        viewPager.setAdapter(adapter);
    }

    private void startCreateProjectDialogFragment(){
        CreateProjectDialogFragment dialog = new CreateProjectDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Project");
    }

    private void startCreateEmployeeDialogFragment(){
        CreateEmployeeDialogFragment dialog = new CreateEmployeeDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Employee");
    }

    private void startCreateControlDeviceDialogFragment(){
        CreateControlDeviceDialogFragment dialog = new CreateControlDeviceDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Controllable Device");
    }

    private void startCreateMachineDialogFragment() {
        CreateMachineDialogFragment dialog = new CreateMachineDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Machine");
    }

    private void startCreateInventoryDialogFragment() {
        CreateInventoryDialogFragment dialog = new CreateInventoryDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Inventory");
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                mFabOpenDialogFragmentProject.show();
                mFabOpenDialogFragmentControlDevice.hide();
                mFabOpenDialogFragmentMachine.hide();
                mFabOpenDialogFragmentInventory.hide();
                break;
            case 1:
                mFabOpenDialogFragmentProject.hide();
                mFabOpenDialogFragmentControlDevice.hide();
                mFabOpenDialogFragmentMachine.hide();
                mFabOpenDialogFragmentInventory.hide();
                break;
            case 2:
                mFabOpenDialogFragmentControlDevice.show();
                mFabOpenDialogFragmentProject.hide();
                mFabOpenDialogFragmentMachine.hide();
                mFabOpenDialogFragmentInventory.hide();
                break;
            case 3:
                mFabOpenDialogFragmentMachine.show();
                mFabOpenDialogFragmentProject.hide();
                mFabOpenDialogFragmentControlDevice.hide();
                mFabOpenDialogFragmentInventory.hide();
                break;
            case 4:
                mFabOpenDialogFragmentMachine.hide();
                mFabOpenDialogFragmentProject.hide();
                mFabOpenDialogFragmentControlDevice.hide();
                mFabOpenDialogFragmentInventory.show();
            default:
                break;
        }
    }

}