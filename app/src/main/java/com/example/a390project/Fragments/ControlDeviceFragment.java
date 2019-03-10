package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.a390project.DummyDatabase;
import com.example.a390project.FirebaseHelper;
import com.example.a390project.ListViewAdapters.ControlDeviceListViewAdapter;
import com.example.a390project.Model.ControlDevice;
import com.example.a390project.R;

import java.util.List;

public class ControlDeviceFragment extends Fragment {

    private View view;
    private List<ControlDevice> cDevice;
    private FirebaseHelper firebaseHelper;
    private EditText cDeviceTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.control_device_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        firebaseHelper = new FirebaseHelper();
        firebaseHelper.populateControlDevices(view, getActivity());
    }


}
