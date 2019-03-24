package com.example.a390project.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.FirebaseHelper;
import com.example.a390project.ForegroundServices.GraphForegroundService;
import com.example.a390project.Model.GraphData;
import com.example.a390project.R;
import com.jjoe64.graphview.GraphView;

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

    //public constructor
    public GraphableProjectsListViewAdapter(Context context, List<String> items, FloatingActionButton mFab) {
        this.context = context;
        this.items = items;
        this.mFab = mFab;
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
                //process checked project and create graphs
                final Intent serviceIntent = new Intent(context, GraphForegroundService.class);
                //values passed to foreground service
                serviceIntent.putStringArrayListExtra("checkedProjectPOs",(ArrayList<String>)checkedProjectPOs);
                int GRAPH_NOTIFICATION_ID = generateRandomInteger();
                serviceIntent.putExtra("GRAPH_NOTIFICATION_ID", GRAPH_NOTIFICATION_ID);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Toast.makeText(context, "Starting GraphForegroundService.", Toast.LENGTH_SHORT).show();
                    context.startForegroundService(serviceIntent);
                }
                else {
                    //context.startService(serviceIntent);
                }
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
