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

    private boolean [] switches = new boolean[2];

    //views
    private TextView textControlDevice;
    private Switch switchControlDevice;

    public ControlDeviceListViewAdapter(Context context, List<ControlDevice> cDevice){
        this.context = context;
        this.cDevice = cDevice;
        switches[0] = false;
        switches[1] = false;
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
        textControlDevice = convertView.findViewById(R.id.control_device_title_text_view_row);
        final ControlDevice currentItem = (ControlDevice) getItem(position);
        textControlDevice.setText(currentItem.getcDeviceTitle());

        //sets the switch based on status stored on firebase for all the control devices
        final FirebaseHelper firebaseHelper = new FirebaseHelper();
        switchControlDevice = convertView.findViewById(R.id.control_device_switch);

        firebaseHelper.setStatusOfSwitch(currentItem.getcDeviceTitle(), switchControlDevice, switches);

        firebaseHelper.canToggleSwitch(switchControlDevice, context);

        //Upon clicking the switch, change the status on firebase
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (switches[0] && switches[1]) {
                        switchControlDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                FirebaseHelper firebaseHelper = new FirebaseHelper();
                                if (isChecked) {
                                    firebaseHelper.changeDeviceStatus(currentItem.getcDeviceTitle(), true, context);
                                } else {
                                    firebaseHelper.changeDeviceStatus(currentItem.getcDeviceTitle(), false, context);
                                }
                            }
                        });
                    }
                }
            }
        });
        t1.start();

        return convertView;

    }


}
