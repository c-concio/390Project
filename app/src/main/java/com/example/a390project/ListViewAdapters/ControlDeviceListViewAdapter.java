package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.a390project.Model.ControlDevice;
import com.example.a390project.R;

import java.util.List;

public class ControlDeviceListViewAdapter extends BaseAdapter {

    private static final String TAG = "CDeviceListViewAdapter";
    private Context context;
    private List<ControlDevice> cDevice;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d(TAG, "getView: gotView");

        if(convertView ==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item_control_device, parent, false);
        }

        setupUI(convertView);

        textControlDevice.setText(cDevice.get(position).getcDeviceTitle());
        if(cDevice.get(position).iscDeviceStatus() == true){
            switchControlDevice.setChecked(true);
        }
        else{
            switchControlDevice.setChecked(false);
        }
        return convertView;
    }

    private void setupUI(View view){
        textControlDevice = view.findViewById(R.id.control_device_title_text_view_row);
        switchControlDevice = view.findViewById(R.id.control_device_switch);
    }



}
