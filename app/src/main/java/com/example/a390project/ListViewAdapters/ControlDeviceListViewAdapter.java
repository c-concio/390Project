package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.Model.ControlDevice;
import com.example.a390project.Model.Task;
import com.example.a390project.R;

import java.util.List;

public class ControlDeviceListViewAdapter extends BaseAdapter {

    private static final String TAG = "CDeviceListViewAdapter";
    private Context context;
    private List<ControlDevice> cDevice;
    private int i = 0;

    //views
    private TextView textControlDevice;
    private Switch switchControlDevice;

    public ControlDeviceListViewAdapter(Context context, List<ControlDevice> cDevice){
        this.context = context;
        this.cDevice = cDevice;
    }

    @Override
    public int getCount(){
        return cDevice.size();
    }

    @Override
    public Object getItem(int position) {
        return cDevice.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView ==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_control_device, parent, false);
        }

        textControlDevice = convertView.findViewById(R.id.control_device_title_text_view_row);

        final FirebaseHelper firebaseHelper = new FirebaseHelper();
        final ControlDevice currentItem = (ControlDevice) getItem(position);
        textControlDevice.setText(currentItem.getcDeviceTitle());

        //sets the switch based on status stored on firebase for all the control devices
        firebaseHelper.setStatusOfSwitch(currentItem.getcDeviceTitle(), convertView);

        //Upon clicking the switch, change the status on firebase
        switchControlDevice = convertView.findViewById(R.id.control_device_switch);
        switchControlDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (i > 2) {
                    if (isChecked) {
                        firebaseHelper.changeDeviceStatus(currentItem.getcDeviceTitle(), true);
                    } else {
                        firebaseHelper.changeDeviceStatus(currentItem.getcDeviceTitle(), false);
                    }
                }
                else {
                    i++;
                }
                Log.d(TAG, "onCheckedChanged: " + i);
            }
        });

        return convertView;

    }


}
