/*
package com.example.a390project.Controllers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.ListViewAdapters.MachineListViewAdapter;
import com.example.a390project.Model.Machine;
import com.example.a390project.Model.Oven;
import com.example.a390project.Model.Paintbooth;
import com.example.a390project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MachineFirebaseHelper {
    // class variables
    DatabaseReference rootRef= FirebaseDatabase.getInstance.getReference();


    public void createMachine(Machine machine){
        rootRef.child("machines").child(machine.getMachineTitle()).setValue(machine);
    }


    public void populateMachine(final View view, final Activity activity) {
        rootRef.child("machines").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                machines = new ArrayList<Machine>();
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    String machineTitle = ds.child("machineTitle").getValue(String.class);
                    String machineLastEmployee =  ds.child("machineLastEmployee").getValue(String.class);
                    String machineType = ds.child("machineType").getValue(String.class);
                    boolean machineStatus = ds.child("machineStatus").getValue(boolean.class);
                    float temperature = ds.child("temperature").getValue(float.class);

                    if (machineType.equals("Oven")) {
                        long machineStatusTimeOff = ds.child("machineStatusTimeOff").getValue(long.class);
                        long machineStatusTimeOn = ds.child("machineStatusTimeOn").getValue(long.class);
                        machines.add(new Oven(machineTitle, machineLastEmployee, machineStatus, temperature, machineType,
                                machineStatusTimeOff, machineStatusTimeOn));
                    }
                    else if (machineType.equals("PaintBooth")) {
                        float humidity = ds.child("humidity").getValue(float.class);
                        machines.add(new Paintbooth(machineTitle, machineLastEmployee, machineStatus, temperature, machineType,
                                humidity));
                    }
                }
                callMachineListViewAdapter(view,activity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callMachineListViewAdapter(View view, Activity activity) {
        // instantiate the custom list adapter
        MachineListViewAdapter adapter = new MachineListViewAdapter(activity, machines);

        // get the ListView and attach the adapter
        ListView itemsListView  = (ListView) view.findViewById(R.id.machine_list_view);
        itemsListView.setAdapter(adapter);
    }
}
*/
