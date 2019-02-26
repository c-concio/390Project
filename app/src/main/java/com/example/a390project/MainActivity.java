package com.example.a390project;

import android.annotation.SuppressLint;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.a390project.DialogFragments.CreateEmployeeDialogFragment;
import com.example.a390project.DialogFragments.CreateMachineDialogFragment;
import com.example.a390project.Fragments.EmployeeFragment;
import com.example.a390project.Fragments.MachineFragment;
import com.example.a390project.Fragments.ProjectFragment;

public class MainActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    //views
    FloatingActionButton mFabOpenDialogFragmentMachine;
    FloatingActionButton mFabOpenDialogFragmentEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate fragment views
        mFabOpenDialogFragmentMachine = findViewById(R.id.fab_open_dialog_fragment_machine);
        mFabOpenDialogFragmentMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateMachineDialogFragment();
            }
        });

        mFabOpenDialogFragmentEmployee = findViewById(R.id.fab_open_dialog_fragment_employee);
        mFabOpenDialogFragmentEmployee.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startCreateEmployeeDialogFragment();
            }
        });

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

        // ------------------ MACHINE FRAGMENT ------------------
        MachineFragment machineFragment = new MachineFragment();
        adapter.addFragment(machineFragment,"MACHINES");

        // ------------------ EMPLOYEE FRAGMENT ------------------
        EmployeeFragment employeeFragment = new EmployeeFragment();
        adapter.addFragment(employeeFragment, "EMPLOYEES");

        // ------------------ PROJECT FRAGMENT -------------------
        ProjectFragment projectFragment = new ProjectFragment();
        adapter.addFragment(projectFragment,"Projects");


        viewPager.setAdapter(adapter);
    }

    private void startCreateMachineDialogFragment() {
        CreateMachineDialogFragment dialog = new CreateMachineDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Group");
    }

    private void startCreateEmployeeDialogFragment(){
        CreateEmployeeDialogFragment dialog = new CreateEmployeeDialogFragment();
        dialog.show(getSupportFragmentManager(), "Create Group");
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                mFabOpenDialogFragmentMachine.show();
                mFabOpenDialogFragmentEmployee.hide();
                break;
            case 1:
                mFabOpenDialogFragmentMachine.hide();
                mFabOpenDialogFragmentEmployee.show();
                break;

            default:
                break;
        }
    }
}
