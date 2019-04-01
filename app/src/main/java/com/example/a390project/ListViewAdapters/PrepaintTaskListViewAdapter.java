package com.example.a390project.ListViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.SubTask;
import com.example.a390project.R;

import java.util.List;

public class PrepaintTaskListViewAdapter extends BaseAdapter {

    private List<SubTask> subTasks;
    private Context context;
    private Activity activity;

    private static final String TAG = "PrepaintTaskListViewAda";

    // row item widgets
    private TextView prepaintNameTextView;
    private EditText descriptionEditText;
    private Button startTimeButton;
    private Button endTimeButton;
    private EditText employeeCommentEditText;
    private TextView mTimeElapsed;
    private boolean backPressed;
    private PrepaintTaskListViewAdapter adapter;

    public PrepaintTaskListViewAdapter(Context context, List<SubTask> subTasks, Activity activity, boolean backPressed){
        this.context = context;
        this.subTasks = subTasks;
        this.activity = activity;
        this.backPressed = backPressed;
        adapter = this;
        Log.d(TAG, "PrepaintTaskListViewAdapter: task size: " + this.subTasks.size());
    }

    @Override
    public int getCount() {
        return subTasks.size();
    }

    @Override
    public SubTask getItem(int position) {
        return subTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_prepaint_tasks, parent, false);
        }

        final SubTask currentItem = (SubTask) getItem(position);

        prepaintNameTextView = convertView.findViewById(R.id.prepaintNameTextView);
        descriptionEditText = convertView.findViewById(R.id.descriptionEditText);
        startTimeButton = convertView.findViewById(R.id.startTimeButton);
        endTimeButton = convertView.findViewById(R.id.endTimeButton);
        employeeCommentEditText = convertView.findViewById(R.id.employeeCommentEditText);
        mTimeElapsed = convertView.findViewById(R.id.elapsed_time_since_pressed_start_time_prepaint);
        mTimeElapsed.setVisibility(View.GONE);

        final FirebaseHelper firebaseHelper = new FirebaseHelper();

        firebaseHelper.setStartTimeEndTimeButtons(startTimeButton,endTimeButton,currentItem.getTaskID());

        firebaseHelper.checkIfTaskStartedAlready(currentItem.getTaskID(), mTimeElapsed, activity, backPressed, convertView, currentItem.getSubTaskType());

        final View finalConvertView = convertView;
        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.checkIfCanStart(currentItem.getTaskID(), context, subTasks.get(position).getSubTaskType(), activity, startTimeButton, endTimeButton, mTimeElapsed, backPressed, finalConvertView);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHelper.checkIfCanEnd(currentItem.getTaskID(), context, mTimeElapsed, finalConvertView);
            }
        });

        prepaintNameTextView.setText(subTasks.get(position).getSubTaskType());

        Log.d(TAG, "getView: got view");

        return convertView;
    }
}
