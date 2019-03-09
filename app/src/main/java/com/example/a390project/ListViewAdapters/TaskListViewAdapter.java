package com.example.a390project.ListViewAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.a390project.Model.Machine;
import com.example.a390project.Model.Project;
import com.example.a390project.Model.Task;
import com.example.a390project.R;

import java.util.List;

public class TaskListViewAdapter extends BaseAdapter {

    private String TAG = "TaskListAdapter";
    private Context context; //context
    private List<Task> items; //data source of the list adapter

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
                    inflate(R.layout.row_item_machine, parent, false);
        }

        // get current item to be displayed
        final Task currentItem = (Task) getItem(position);

        //set views of row item


        return convertView;
    }
}
