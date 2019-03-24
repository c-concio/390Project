package com.example.a390project.ForegroundServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class GraphForegroundService extends Service {
    public static final String GRAPH_CHANNEL_ID = "GraphChannel";
    private static final String TAG = "GraphForegroundService";

    //varaibles from intent
    private ArrayList<String> checkedProjectPOs;
    private int GRAPH_NOTIFICATION_ID;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkedProjectPOs = intent.getStringArrayListExtra("checkedProjectPOs");
        GRAPH_NOTIFICATION_ID = intent.getIntExtra("GRAPH_NOTIFICATION_ID",0);

        //check that values passed from intent are not null/0

        if (!(checkedProjectPOs == null && GRAPH_NOTIFICATION_ID == 0)) {
            //make the pending intent

        }





        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
