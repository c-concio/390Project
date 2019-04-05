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

    //views
    private TextView textControlDevice;
    private Switch switchControlDevice;
    private FirebaseHelper firebaseHelper;

    public ControlDeviceListViewAdapter(Context context, List<ControlDevice> cDevice){
        this.context = context;
        this.cDevice = cDevice;
        firebaseHelper = new FirebaseHelper();
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
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_control_device, parent, false);
        }
        final ControlDevice currentItem = (ControlDevice) getItem(position);

        textControlDevice = convertView.findViewById(R.id.control_device_title_text_view_row);
        textControlDevice.setText(currentItem.getcDeviceTitle());
        switchControlDevice = convertView.findViewById(R.id.control_device_switch);

        firebaseHelper.setStatusOfSwitch(currentItem.getcDeviceTitle(), switchControlDevice);
        firebaseHelper.canToggleSwitch(switchControlDevice, context);

        final View finalConvertView = convertView;
        switchControlDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchControlDevice = finalConvertView.findViewById(R.id.control_device_switch);
                if (switchControlDevice.isChecked()) {
                    firebaseHelper.changeDeviceStatus(currentItem.getcDeviceTitle(), true, context);
                } else {
                    firebaseHelper.changeDeviceStatus(currentItem.getcDeviceTitle(), false, context);
                }
            }
        });

        return convertView;

    }


}
