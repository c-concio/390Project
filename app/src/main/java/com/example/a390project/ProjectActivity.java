package com.example.a390project;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a390project.DialogFragments.CompleteProjectDialogFragment;
import com.example.a390project.DialogFragments.CreateMachineDialogFragment;
import com.example.a390project.DialogFragments.CreateTaskDialogFragment;
import com.example.a390project.Fragments.EmployeeTasksFragment;
import com.example.a390project.Fragments.EmployeeWorkBlocksFragment;
import com.example.a390project.Fragments.ProjectGraphFragment;
import com.example.a390project.Fragments.ProjectTasksFragment;
import com.example.a390project.Model.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private static final String TAG = "ProjectActivity";

    //This is our tab-layout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    //views
    private FloatingActionButton mFabOpenTaskDialogFragment;
    //variables
    private String projectPO;
    //check if user is manager from sharedpreferences
    private boolean isManager;

    private boolean tasksCompleted = false;
    private boolean projectCompleted = false;

    private ValueEventListener tasksCompletedValueEventListener;

    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        checkIfManager();
        firebaseHelper = new FirebaseHelper();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tasksCompletedValueEventListener = firebaseHelper.isTasksCompleted(projectPO, this);

    }

    private void checkIfManager() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String manager = preferences.getString("isManager",null);
        if (manager.equals("true")) {
            isManager = true;
            prepareActivity(isManager);
        }
        else {
            isManager = false;
            prepareActivity(isManager);
        }
    }

    private void prepareActivity(final boolean isManager) {
        upNavigation();
        projectPO = getIntent().getStringExtra("projectPO");
        mFabOpenTaskDialogFragment = findViewById(R.id.fab_open_dialog_fragment_task);
        if (isManager) {
            mFabOpenTaskDialogFragment.show();
            mFabOpenTaskDialogFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCreateTaskDialogFragment();
                }
            });

        }
        else {
            mFabOpenTaskDialogFragment.hide();
        }

        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager_project);
        viewPager.setOffscreenPageLimit(5); // <---- .setOffscreenPageLimit controls the max amount of tabs

        //Initializing the tablayout
        tabLayout = findViewById(R.id.tablayout_project);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (isManager)
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

        ProjectTasksFragment projectTasksFragment = new ProjectTasksFragment(projectPO);
        adapter.addFragment(projectTasksFragment, "PROJECT TASKS");

        ProjectGraphFragment projectGraphFragment = new ProjectGraphFragment(projectPO);
        adapter.addFragment(projectGraphFragment, "PROJECT GRAPHS");

        viewPager.setAdapter(adapter);
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                if (projectCompleted)
                    mFabOpenTaskDialogFragment.hide();
                else
                    mFabOpenTaskDialogFragment.show();
                break;
            case 1:
                mFabOpenTaskDialogFragment.hide();
                break;
            default:
                break;
        }
    }


    private void startCreateTaskDialogFragment() {
        CreateTaskDialogFragment dialog = new CreateTaskDialogFragment(projectPO);
        dialog.show(getSupportFragmentManager(), "Create Task");
    }

    private void upNavigation() {
        // provide up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("projectTitle"));
    }

    //up navigation

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
            case R.id.generatePdfItem:
                // generate the pdf for the current project
                PdfHelper pdfHelper = new PdfHelper(2200, 1700, this);
                pdfHelper.generatePdf(projectPO);
                return true;
            case R.id.completeProjectItem:
                // if tasks are completed then set onclick to complete the project, else invalidate
                CompleteProjectDialogFragment completeProjectDialogFragment = new CompleteProjectDialogFragment();
                completeProjectDialogFragment.setCompletedTasks(tasksCompleted);
                completeProjectDialogFragment.setCompletedProject(projectCompleted);
                completeProjectDialogFragment.setProjectPO(projectPO);
                completeProjectDialogFragment.show(getSupportFragmentManager(), "CompleteProjectDialogFragment");

                Log.d(TAG, "onOptionsItemSelected: tsksCompleted " + tasksCompleted);
                Log.d(TAG, "onOptionsItemSelected: projectCompleted " + projectCompleted);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // create the a menu item for pdf generation
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_project, menu);

        MenuItem completeProjectItem = menu.findItem(R.id.completeProjectItem);
        // show completeProjectItem only if manager
        if (isManager){
            completeProjectItem.setVisible(true);
        }
        else{
            completeProjectItem.setVisible(false);
        }


        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseHelper.detatchTasksCompletedValueEventListener(tasksCompletedValueEventListener);
    }

    protected void setTasksCompleted(boolean tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public void setProjectCompleted(boolean projectCompleted) {
        this.projectCompleted = projectCompleted;
    }
}
