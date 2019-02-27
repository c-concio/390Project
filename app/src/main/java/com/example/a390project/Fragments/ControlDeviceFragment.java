package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.a390project.DummyDatabase;
import com.example.a390project.ListViewAdapters.ControlDeviceListViewAdapter;
import com.example.a390project.Model.ControlDevice;
import com.example.a390project.R;

import java.util.List;

public class ControlDeviceFragment extends Fragment {

    private View view;
    private List<ControlDevice> cDevice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.control_device_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;

        //Two fixed devices
        cDevice = new DummyDatabase().generateDummyControlDevice();
        callListViewAdapter(view, cDevice);
    }

    private void callListViewAdapter(View view, List<ControlDevice> cDevice){
        ControlDeviceListViewAdapter adapter = new ControlDeviceListViewAdapter(getActivity(), cDevice);

        ListView itemsListView = (ListView) view.findViewById(R.id.control_device_list_view);
        itemsListView.setAdapter(adapter);
    }


}
