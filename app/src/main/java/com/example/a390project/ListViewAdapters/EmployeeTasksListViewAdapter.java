package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.Model.EmployeeTasks;
import com.example.a390project.Model.Task;
import com.example.a390project.ProjectActivity;
import com.example.a390project.R;
import com.example.a390project.TaskBakingActivity;
import com.example.a390project.TaskInspectionActivity;
import com.example.a390project.TaskPackagingActivity;
import com.example.a390project.TaskPaintingActivity;
import com.example.a390project.TaskPrePaintingActivity;
import com.google.android.gms.tasks.Tasks;

import java.util.List;

public class EmployeeTasksListViewAdapter extends BaseAdapter {

    // views
    private TextView taskTextView;
    private TextView projectPOTextView;

    private static final String TAG = "EmployeeTasksListViewAd";

    // UI components
    private List<Task> tasks;
    private Context context;

    public EmployeeTasksListViewAdapter(Context context, List<Task> tasks){
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_item_employee_tasks, parent, false);
        }

        final Task currentItem =(Task) getItem(position);

        setupUI(convertView);

        taskTextView.setText(tasks.get(position).getTaskType());
        projectPOTextView.setText(tasks.get(position).getProjectPO());

        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskType = currentItem.getTaskType();
                Log.d(TAG, "onClick: " + taskType);

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
                    context.startActivity(intent);
                }
                else if(taskType.equals("Painting")) {
                    Intent intent = new Intent(context, TaskPaintingActivity.class);
                    intent.putExtra("paintingTaskID", currentItem.getTaskID());
                    context.startActivity(intent);
                }
                else if(taskType.equals("Baking")) {
                    Intent intent = new Intent(context, TaskBakingActivity.class);
                    intent.putExtra("bakingTaskID", currentItem.getTaskID());
                    Log.d(TAG, "onClick: " + currentItem.getTaskID());
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

        return convertView;
    }

    private void setupUI(View view){

        taskTextView = view.findViewById(R.id.taskTextView);
        projectPOTextView = view.findViewById(R.id.projectPOTextView);
    }

}
