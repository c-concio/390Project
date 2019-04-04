package com.example.a390project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.a390project.Fragments.EmployeeTasksFragment;
import com.example.a390project.Fragments.EmployeeWorkBlocksFragment;



public class EmployeeActivity extends AppCompatActivity{

    //This is our tab-layout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;


    private static final String TAG = "EmployeeActivity";
    private String employeeID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        // provide up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // get intent to get extra
        Intent intent = getIntent();
        employeeID = intent.getStringExtra("employeeID");
        setActionBar(intent.getStringExtra("employeeName"));

        prepareActivity();

    }

    private void prepareActivity() {
        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager_employee);
        viewPager.setOffscreenPageLimit(5); // <---- .setOffscreenPageLimit controls the max amount of tabs

        //Initializing the tablayout
        tabLayout = findViewById(R.id.tablayout_employee);
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

        EmployeeTasksFragment employeeTasksFragment = new EmployeeTasksFragment(employeeID);
        adapter.addFragment(employeeTasksFragment, "TASKS");

        EmployeeWorkBlocksFragment employeeWorkBlocksFragment = new EmployeeWorkBlocksFragment(employeeID);
        adapter.addFragment(employeeWorkBlocksFragment, "WORK BLOCKS");

        viewPager.setAdapter(adapter);
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            default:
                break;
        }
    }

    //up navigation

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

    //custom heading and back button
    public void setActionBar(String heading) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(heading);
        actionBar.show();
    }
}
