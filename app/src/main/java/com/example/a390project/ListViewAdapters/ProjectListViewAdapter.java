package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.Model.Project;
import com.example.a390project.ProjectActivity;
import com.example.a390project.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class ProjectListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Project> projects;

    //views
    private TextView mClient;
    private TextView mTitle;
    private TextView mPO;
    private TextView mStartDate;
    private TextView mDueDate;

    public ProjectListViewAdapter(Context context, List<Project> projects){
        this.context = context;
        this.projects = projects;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_project, parent, false);
        }

        // get current item to be displayed
        final Project currentItem = (Project) getItem(position);

        mClient = convertView.findViewById(R.id.task_type_row_item);
        mTitle = convertView.findViewById(R.id.task_description_row_item);
        mPO = convertView.findViewById(R.id.project_PO);
        mStartDate = convertView.findViewById(R.id.project_startDate);
        mDueDate = convertView.findViewById(R.id.project_dueDate);

        mClient.setText(currentItem.getClient());
        mTitle.setText(currentItem.getTitle());
        mPO.setText(currentItem.getPo());
        mStartDate.setText(getDate(currentItem.getStartDate()));
        mDueDate.setText(getDate(currentItem.getDueDate()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProjectActivity(currentItem.getPo(), currentItem.getTitle());

            }
        });

        return convertView;
    }

    private void startProjectActivity(String po, String pt) {
        Intent intent = new Intent(context, ProjectActivity.class);
        intent.putExtra("projectPO", po);
        intent.putExtra("projectTitle", pt);
        context.startActivity(intent);
    }

    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(time);
    }
}
