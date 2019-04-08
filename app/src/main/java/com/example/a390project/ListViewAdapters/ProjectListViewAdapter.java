package com.example.a390project.ListViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a390project.DialogFragments.DeleteProjectDialogFragment;
import com.example.a390project.FirebaseHelper;
import com.example.a390project.MainActivity;
import com.example.a390project.Model.Project;
import com.example.a390project.ProjectActivity;
import com.example.a390project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProjectListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Project> projects;
    private List<Boolean> projectCompleted;

    //views
    private TextView mClient;
    private TextView mTitle;
    private TextView mPO;
    private TextView mStartDate;
    private TextView mDueDate;
    private ImageView mIcon;

    public ProjectListViewAdapter(Context context, List<Project> projects, List<Boolean> projectCompleted){
        this.context = context;
        this.projects = projects;
        this.projectCompleted = projectCompleted;
    }

    @Override
    public int getCount() {
        return projects.size();
    }

    @Override
    public Object getItem(int position) {
        return projects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_project, parent, false);
        }

        // get current item to be displayed
        final Project currentItem = (Project) getItem(position);

        mClient = convertView.findViewById(R.id.paint_description_row_item);
        mTitle = convertView.findViewById(R.id.paint_code_row_item);
        mPO = convertView.findViewById(R.id.paint_type_row_item);
        mStartDate = convertView.findViewById(R.id.bake_temperature_row_item);
        mDueDate = convertView.findViewById(R.id.bake_time_row_item);
        mIcon = convertView.findViewById(R.id.working_icon_projects);

        mClient.setText(currentItem.getClient());
        mTitle.setText(currentItem.getTitle());
        mPO.setText(currentItem.getPo());
        mStartDate.setText(getDate(currentItem.getStartDate()));
        mDueDate.setText(getDate(currentItem.getDueDate()));
        mIcon.setVisibility(View.GONE);

        //adjust mIcon based on if task is being currently worked in this project or not by uID
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.checkIfIconAppliesForProjectsListRow(mIcon, currentItem.getPo());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProjectActivity(currentItem.getPo(), currentItem.getTitle());

            }
        });
        long timeNow = System.currentTimeMillis();
        long dueDate = currentItem.getDueDate();
        long sevenDaysFromDueDate = dueDate - 604800000;
        long oneDayFromDueDate = dueDate - 86400000;

        if (timeNow > dueDate)
            convertView.setBackgroundResource(R.color.lightDarkerRed);
        else if (timeNow > oneDayFromDueDate && timeNow < dueDate)
            convertView.setBackgroundResource(R.color.lightRed);
        else if (timeNow > sevenDaysFromDueDate && timeNow < dueDate)
            convertView.setBackgroundResource(R.color.lightYellow);
        else
            convertView.setBackgroundResource(R.color.lightGreen);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DeleteProjectDialogFragment dialog = new DeleteProjectDialogFragment(currentItem.getPo(), currentItem.getTitle());
                dialog.show(((MainActivity) context).getSupportFragmentManager(),"delete_project");
                return true;
            }
        });

        // if the task is completed, then the background should be color white
        if (projects.get(position).getHasCompleted())
            convertView.setBackgroundResource(R.color.white);

        return convertView;
    }

    private void startProjectActivity(String po, String pt) {
        Intent intent = new Intent(context, ProjectActivity.class);
        intent.putExtra("projectPO", po);
        intent.putExtra("projectTitle", pt);
        context.startActivity(intent);
    }

    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
        return formatter.format(time);
    }
}
