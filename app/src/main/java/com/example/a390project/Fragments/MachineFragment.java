package com.example.a390project.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.example.a390project.ListViewAdapters.MachineListViewAdapter;
import com.example.a390project.Machine;
import com.example.a390project.R;

public class MachineFragment extends Fragment {

    //views

    //variables
    private View mView;
    private List<Machine> machines;

    public static final String TAG = "MachineFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.machine_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mView = view;
        //instantiate fragment views

        //Populate the ArrayList machines
        //In further sprints, we would create a controller to fetch machines from firebase database
        machines = new ArrayList<Machine>();
        String [] names = {"Abdulrahim", "Antoine ", "Andrew", "Chris", "Kris"};
        //alternate status to view background effect
        boolean status = false;
        for (int i = 0; i < 5; i++) {
            machines.add(new Machine("Machine " + i, names[i],status));
            status = !status;
        }

        //After populating the machines, populate the mMachineListView
        callListViewAdapter(view,machines);
    }

    //adapter which populates mMachineListView with the ArrayList machines
    private void callListViewAdapter(View view, List<Machine> machines) {
        // instantiate the custom list adapter
        MachineListViewAdapter adapter = new MachineListViewAdapter(getActivity(), machines);

        // get the ListView and attach the adapter
        ListView itemsListView  = (ListView) view.findViewById(R.id.machine_list_view);
        itemsListView.setAdapter(adapter);
    }

}
