package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a390project.MachineActivity;
import com.example.a390project.Model.Machine;

import java.util.List;

import com.example.a390project.R;

public class MachineListViewAdapter extends BaseAdapter {

    private String TAG = "CustomGroupListAdapter";
    private Context context; //context
    private List<Machine> items; //data source of the list adapter

    //views
    TextView mMachineName;
    TextView mMachineLastEmployee;
    TextView mStatus;
    ConstraintLayout mRowItem;

    //public constructor
    public MachineListViewAdapter(Context context, List<Machine> items) {
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
        final Machine currentItem = (Machine) getItem(position);

        //set views of row item
        mMachineName = convertView.findViewById(R.id.machine_title_text_view_row);
        mMachineLastEmployee = convertView.findViewById(R.id.employee_name_text_view);
        mStatus = convertView.findViewById(R.id.status_text_view);
        mRowItem = convertView.findViewById(R.id.machine_row_item);

        mMachineName.setText(currentItem.getMachineTitle());
        mMachineLastEmployee.setText(currentItem.getMachineLastEmployee());
        String status = currentItem.isMachineStatus() ? "On":"Off";
        mStatus.setText("Status: " + status);

        //change background color if On/Off
        if (currentItem.isMachineStatus())
            mRowItem.setBackgroundColor(Color.parseColor("#FFE5E5"));
        else
            mRowItem.setBackgroundColor(Color.parseColor("#DBFFDC"));

        //onClick opens Machine Activity
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String machineID = currentItem.getMachineID();
                String machineTitle = currentItem.getMachineTitle();
                Boolean machineStatus = currentItem.isMachineStatus();
                startMachineActivity(machineID, machineTitle, machineStatus);
            }
        });

        return convertView;
    }
    //Controller will only need to send machineID from MachineListViewAdapter to machineActivity,
    // which will get the entire Machine Object from Firebase
    private void startMachineActivity(String machineID, String machineTitle, boolean machineStatus) {
        Intent intent = new Intent(context, MachineActivity.class);
        intent.putExtra("machine_ID", machineID);
        intent.putExtra("machine_title", machineTitle);
        intent.putExtra("machine_status", machineStatus);
        context.startActivity(intent);
    }
}
