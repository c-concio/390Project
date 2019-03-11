package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a390project.Model.Task;
import com.example.a390project.R;

import java.util.List;

public class PrepaintTaskListViewAdapter extends BaseAdapter {

    private List<Task> tasks;
    private Context context;

    private static final String TAG = "PrepaintTaskListViewAda";

    // row item widgets
    private TextView prepaintNameTextView;
    private EditText descriptionEditText;
    private Button startTimeButton;
    private Button endTimeButton;
    private EditText employeeCommentEditText;

    public PrepaintTaskListViewAdapter(Context context, List<Task> tasks){
        this.context = context;
        this.tasks = tasks;
        Log.d(TAG, "PrepaintTaskListViewAdapter: task size: " + tasks.size());
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_prepaint_tasks, parent, false);
        }

        prepaintNameTextView = convertView.findViewById(R.id.prepaintNameTextView);
        descriptionEditText = convertView.findViewById(R.id.descriptionEditText);
        startTimeButton = convertView.findViewById(R.id.startTimeButton);
        endTimeButton = convertView.findViewById(R.id.endTimeButton);
        employeeCommentEditText = convertView.findViewById(R.id.employeeCommentEditText);

        prepaintNameTextView.setText(tasks.get(position).getPrepaintName());
        descriptionEditText.setText(tasks.get(position).getDescription());
        employeeCommentEditText.setText(tasks.get(position).getEmployeeComment());

        Log.d(TAG, "getView: got view");

        return convertView;
    }
}
