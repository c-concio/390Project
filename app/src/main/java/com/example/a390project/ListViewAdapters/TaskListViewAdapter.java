package com.example.a390project.ListViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.Task;
import com.example.a390project.Model.fragmentInListView;
import com.example.a390project.R;
import com.example.a390project.TaskBakingActivity;
import com.example.a390project.TaskInspectionActivity;
import com.example.a390project.TaskPackagingActivity;
import com.example.a390project.TaskPaintingActivity;
import com.example.a390project.TaskPrePaintingActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskListViewAdapter extends BaseAdapter {

    private String TAG = "TaskListAdapter";
    private Context context; //context
    private List<Task> items; //data source of the list adapter

    //views
    private TextView mTaskType;
    private TextView mCreatedTime;
    private ImageView mIcon;

    //variables
    private String projectPO;

    public TaskListViewAdapter(Context context, List<Task> items, String projectPO) {
        this.context = context;
        this.items = items;
        this.projectPO = projectPO;
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

        Log.d(TAG, "getView: in get view");
        // get current item to be displayed
        final Task currentItem = (Task) getItem(position);

        //set views of row item
        mTaskType = convertView.findViewById(R.id.paint_description_row_item);
        mCreatedTime = convertView.findViewById(R.id.task_created_time_row_item);
        mIcon = convertView.findViewById(R.id.working_icon_tasks);

        mTaskType.setText(currentItem.getTaskType());
        DateFormat df = DateFormat.getDateTimeInstance();
        String dateStringcreatedTime = df.format(currentItem.getCreatedTime());
        mCreatedTime.setText(dateStringcreatedTime);
        mIcon.setVisibility(View.GONE);

        //adjust mIcon based on if task is being currently worked or not by uID
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.checkIfIconAppliesForTasksListRow(mIcon, currentItem.getTaskID());

        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskType = currentItem.getTaskType();

                // 5 possible TaskActivities in total:
                // Inspection & Final-Inspection - Pre-Painting - Painting - Baking - Packaging

                if(taskType.equals("Inspection") || taskType.equals("Final-Inspection")) {
                    Intent intent = new Intent(context, TaskInspectionActivity.class);
                    // send the TaskInspectionActivity the projectPO
                    intent.putExtra("inspectionTaskID", currentItem.getTaskID());
                    intent.putExtra("taskType", taskType);
                    context.startActivity(intent);
                }
                else if(taskType.equals("Pre-Painting")) {
                    Intent intent = new Intent(context, TaskPrePaintingActivity.class);
                    intent.putExtra("prepaintingTaskID", currentItem.getTaskID());
                    intent.putExtra("prepaintingDescription", currentItem.getDescription());
                    context.startActivity(intent);
                }
                else if(taskType.equals("Painting")) {
                    Intent intent = new Intent(context, TaskPaintingActivity.class);
                    intent.putExtra("paintingTaskID",currentItem.getTaskID());
                    context.startActivity(intent);
                }
                else if(taskType.equals("Baking")) {
                    Intent intent = new Intent(context, TaskBakingActivity.class);
                    intent.putExtra("bakingTaskID", currentItem.getTaskID());
                    context.startActivity(intent);
                }
                else if(taskType.equals("Packaging")) {
                    Intent intent = new Intent(context, TaskPackagingActivity.class);
                    // send the taskPackagingActivity the projectPO
                    intent.putExtra("packagingTaskID", currentItem.getTaskID());

                    context.startActivity(intent);
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String Taskid = currentItem.getTaskID();
                fragmentInListView filv = new fragmentInListView(Taskid,projectPO);
                filv.deletetaskfragment(context);
                return true;
            }
        });


        return convertView;
    }


    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(time);
    }
}
