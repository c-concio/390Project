package com.example.a390project.ListViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.EmployeeActivity;
import com.example.a390project.Model.Machine;
import com.example.a390project.Model.Project;
import com.example.a390project.Model.Task;
import com.example.a390project.R;
import com.example.a390project.TaskBakingActivity;
import com.example.a390project.TaskInspectionActivity;
import com.example.a390project.TaskPackagingActivity;
import com.example.a390project.TaskPaintingActivity;
import com.example.a390project.TaskPrePaintingActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class TaskListViewAdapter extends BaseAdapter {

    private String TAG = "TaskListAdapter";
    private Context context; //context
    private List<Task> items; //data source of the list adapter

    //views
    private TextView mTaskType;
    private TextView mCreatedTime;

    public TaskListViewAdapter(Context context, List<Task> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_item_task, parent, false);
        }

        // get current item to be displayed
        final Task currentItem = (Task) getItem(position);

        //set views of row item
        mTaskType = convertView.findViewById(R.id.task_type_row_item);
        mCreatedTime = convertView.findViewById(R.id.task_created_time_row_item);

        mTaskType.setText(currentItem.getTaskType());
        mCreatedTime.setText(getDate(currentItem.getCreatedTime()));

        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskType = currentItem.getTaskType();

                // 5 possible TaskActivities in total:
                // Inspection & Final-Inspection - Pre-Painting - Painting - Baking - Packaging

                if(taskType.equals("Inspection") || taskType.equals("Final-Inspection")) {
                    Intent intent = new Intent(context, TaskInspectionActivity.class);
                    context.startActivity(intent);
                }
                else if(taskType.equals("Pre-Painting")) {
                    Intent intent = new Intent(context, TaskPrePaintingActivity.class);
                    context.startActivity(intent);
                }
                else if(taskType.equals("Painting")) {
                    Intent intent = new Intent(context, TaskPaintingActivity.class);
                    context.startActivity(intent);
                }
                else if(taskType.equals("Baking")) {
                    Intent intent = new Intent(context, TaskBakingActivity.class);
                    context.startActivity(intent);
                }
                else if(taskType.equals("Packaging")) {
                    Intent intent = new Intent(context, TaskPackagingActivity.class);
                    context.startActivity(intent);
                }
            }
        });


        return convertView;
    }

    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(time);
    }
}
