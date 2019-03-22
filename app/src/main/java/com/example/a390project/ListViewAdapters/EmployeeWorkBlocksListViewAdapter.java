package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.Model.Machine;
import com.example.a390project.Model.WorkBlock;
import com.example.a390project.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class EmployeeWorkBlocksListViewAdapter extends BaseAdapter {
    private String TAG = "EWorkBlockListAdapter";

    private Context context; //context
    private List<WorkBlock> items; //data source of the list adapter

    //views
    private TextView mTitle;
    private TextView mProjectPO;
    private TextView mStartTime;
    private TextView mEndTime;
    private TextView mDurationTime;

    //public constructor
    public EmployeeWorkBlocksListViewAdapter(Context context, List<WorkBlock> items) {
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
                    inflate(R.layout.row_item_work_blocks, parent, false);
        }

        // get current item to be displayed
        final WorkBlock currentItem = (WorkBlock) getItem(position);

        //set views of row item
        mTitle = convertView.findViewById(R.id.work_block_title);
        mProjectPO = convertView.findViewById(R.id.projectPO_work_block);
        mStartTime = convertView.findViewById(R.id.start_time_work_block);
        mEndTime = convertView.findViewById(R.id.end_time_work_block);
        mDurationTime = convertView.findViewById(R.id.duration_time_work_block);

        mTitle.setText(currentItem.getTitle());
        mProjectPO.setText(currentItem.getProjectPO());

        DateFormat df = DateFormat.getDateTimeInstance();
        String dateStringstartTime = df.format(currentItem.getStartTime());
        String dateStringendTime = df.format(currentItem.getEndTime());

        mStartTime.setText(dateStringstartTime);
        mEndTime.setText(dateStringendTime);
        long milliSec = currentItem.getWorkingTime();
        long sec = (milliSec/1000) % 60;
        long min = ((milliSec/1000) / 60) % 60;
        long hour = ((milliSec/1000) / 60) / 60;
        mDurationTime.setText(hour+"h"+min+"m"+sec+"s");

        return convertView;
    }

    private String getDate(long time) {
        String value;

        if(time == 0) {
            value = "-";
        }
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
            value = formatter.format(time);
        }

        return value;
    }
}
