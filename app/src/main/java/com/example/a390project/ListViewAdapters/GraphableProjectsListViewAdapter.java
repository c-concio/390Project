package com.example.a390project.ListViewAdapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.ForegroundServices.GFSOvenGraphForegroundService;
import com.example.a390project.ForegroundServices.BigOvenGraphForegroundService;
import com.example.a390project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphableProjectsListViewAdapter extends BaseAdapter {

    private Context context; //context
    private List<String> items; //data source of the list adapter
    private List<String> checkedProjectPOs;

    //views
    private TextView projectPO;
    private CheckBox checkBox;
    private FloatingActionButton mFab;
    private Dialog dialog;
    private AppCompatEditText mTitle;

    //variables
    private String machineTitle;
    private boolean machineStatus;

    //public constructor
    public GraphableProjectsListViewAdapter(Context context, List<String> items, FloatingActionButton mFab,
                                            Dialog dialog, String machineTitle, boolean machineStatus, AppCompatEditText mTitle) {
        this.context = context;
        this.items = items;
        this.mFab = mFab;
        this.dialog = dialog;
        this.machineTitle = machineTitle;
        this.machineStatus = machineStatus;
        this.mTitle = mTitle;
        checkedProjectPOs = new ArrayList<>();
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
                    inflate(R.layout.row_item_graphable_projects, parent, false);
        }

        // get current item to be displayed
        final String currentItem = (String) getItem(position);

        projectPO = convertView.findViewById(R.id.graphable_projectPO);
        checkBox = convertView.findViewById(R.id.graphable_checkbox);

        projectPO.setText(currentItem);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(context, currentItem + " checked!", Toast.LENGTH_SHORT).show();
                    checkedProjectPOs.add(currentItem);
                }
                else {
                    checkedProjectPOs.remove(currentItem);
                }
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if graph title is empty
                final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.child("machines").child(machineTitle).child("graphingStatus").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        rootRef.child("machines").child(machineTitle).child("graphingStatus").removeEventListener(this);
                        if (!dataSnapshot.exists() || !dataSnapshot.getValue(boolean.class)) {
                            String graphTitle = mTitle.getText().toString().trim();
                            if (!graphTitle.isEmpty()) {
                                //process checked project and create graphs
                                final Intent serviceIntent;
                                if (machineTitle.equals("Big_Oven")) {
                                    serviceIntent = new Intent(context, BigOvenGraphForegroundService.class);
                                }
                                else {
                                    serviceIntent = new Intent(context, GFSOvenGraphForegroundService.class);
                                }

                                int GRAPH_NOTIFICATION_ID = generateRandomInteger();

                                //values passed to foreground service
                                serviceIntent.putStringArrayListExtra("checkedProjectPOs", (ArrayList<String>) checkedProjectPOs);
                                serviceIntent.putExtra("GRAPH_NOTIFICATION_ID", GRAPH_NOTIFICATION_ID);
                                serviceIntent.putExtra("machineTitle", machineTitle);
                                serviceIntent.putExtra("machineStatus", machineStatus);
                                serviceIntent.putExtra("graphTitle", graphTitle);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    Toast.makeText(context, "Starting BigOvenGraphForegroundService.", Toast.LENGTH_SHORT).show();
                                    context.startForegroundService(serviceIntent);
                                    dialog.dismiss();
                                } else {
                                    //context.startService(serviceIntent);
                                }
                            } else {
                                Toast.makeText(context, "Enter Graph Title...", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(context, "Already Graphing...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return convertView;
    }

    public int generateRandomInteger() {
        int length = 8;
        String candidateChars = "123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }

        return Integer.parseInt(sb.toString());
    }
}
