package com.example.a390project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.a390project.DialogFragments.CreateControlDeviceDialogFragment;
import com.example.a390project.DialogFragments.CreateEmployeeDialogFragment;
import com.example.a390project.DialogFragments.CreateMachineDialogFragment;
import com.example.a390project.DialogFragments.CreateProjectDialogFragment;
import com.example.a390project.Fragments.ControlDeviceFragment;
import com.example.a390project.Fragments.EmployeeFragment;
import com.example.a390project.Fragments.MachineFragment;
import com.example.a390project.Fragments.ProjectFragment;
import com.example.a390project.Model.ControlDevice;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    //This is our tab-layout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    //views
    FloatingActionButton mFabOpenDialogFragmentMachine;
    FloatingActionButton mFabOpenDialogFragmentControlDevice;
    FloatingActionButton mFabOpenDialogFragmentProject;
    //firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            Log.d(TAG,"No user logged in or registered");
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }
        else {
            prepareActivity();
            Toast.makeText(this,"Welcome back",Toast.LENGTH_SHORT).show();
        }
    }

    private void prepareActivity() {
        //instantiate fragment views
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

        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4); // <---- .setOffscreenPageLimit controls the max amount of tabs

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
                animateFab(position);
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
        ProjectFragment projectFragment = new ProjectFragment();
        adapter.addFragment(projectFragment, "PROJECTS");

        // ------------------ EMPLOYEE FRAGMENT ------------------
        EmployeeFragment employeeFragment = new EmployeeFragment();
        adapter.addFragment(employeeFragment, "EMPLOYEES");

        // ------------------ CONTROL DEVICE FRAGMENT ------------------
        ControlDeviceFragment controlDeviceFragment = new ControlDeviceFragment();
        adapter.addFragment(controlDeviceFragment, "DEVICES");

        // ------------------ MACHINE FRAGMENT ------------------
        MachineFragment machineFragment = new MachineFragment();
        adapter.addFragment(machineFragment,"MACHINES");


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

    private void animateFab(int position) {
        switch (position) {
            case 0:
                mFabOpenDialogFragmentProject.show();
                mFabOpenDialogFragmentControlDevice.hide();
                mFabOpenDialogFragmentMachine.hide();
                break;
            case 1:
                mFabOpenDialogFragmentProject.hide();
                mFabOpenDialogFragmentControlDevice.hide();
                mFabOpenDialogFragmentMachine.hide();
                break;
            case 2:
                mFabOpenDialogFragmentControlDevice.show();
                mFabOpenDialogFragmentProject.hide();
                mFabOpenDialogFragmentMachine.hide();
                break;
            case 3:
                mFabOpenDialogFragmentMachine.show();
                mFabOpenDialogFragmentProject.hide();
                mFabOpenDialogFragmentControlDevice.hide();
                break;
            default:
                break;
        }
    }

}
