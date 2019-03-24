package com.example.a390project.ForegroundServices;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.a390project.MachineActivity;
import com.example.a390project.R;

import java.util.ArrayList;

public class GraphForegroundService extends Service {
    public static final String GRAPH_CHANNEL_ID = "GraphChannel";
    private static final String TAG = "GraphForegroundService";

    //variables from intent
    private ArrayList<String> checkedProjectPOs;
    private int GRAPH_NOTIFICATION_ID;
    private String machineTitle;
    private boolean machineStatus;

    private NotificationCompat.Builder builder = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(builder != null && GRAPH_NOTIFICATION_ID != 0) {
                        startForeground(GRAPH_NOTIFICATION_ID, builder.build());
                        Log.d(TAG, "run: GRAPH FOREGROUND STARTED");
                        break;
                    }
                }
            }
        });
        t.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkedProjectPOs = intent.getStringArrayListExtra("checkedProjectPOs");
        GRAPH_NOTIFICATION_ID = intent.getIntExtra("GRAPH_NOTIFICATION_ID",0);
        machineTitle = intent.getStringExtra("machineTitle");
        machineStatus = intent.getBooleanExtra("machineStatus",false);

        //create intent to be put as a pending intent activated when notification clicked
        Intent intent2 = new Intent(getApplicationContext(), MachineActivity.class);
        // send the TaskInspectionActivity the projectPO
        intent2.putExtra("machine_title", machineTitle);
        intent2.putExtra("machine_status", machineStatus);

        //check that values passed from intent are not null/0

        if (checkedProjectPOs != null && GRAPH_NOTIFICATION_ID != 0) {
            final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), GRAPH_NOTIFICATION_ID, intent2, 0);
            final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            String list_of_projects_POs = "";
            int i = 0;
            for (String checkedProjectPO:checkedProjectPOs) {
                if (i < checkedProjectPOs.size()-1)
                    list_of_projects_POs += checkedProjectPO + "\n";
                else
                    list_of_projects_POs += checkedProjectPO;
                i++;
            }

            builder = new NotificationCompat.Builder(this, GRAPH_CHANNEL_ID)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(list_of_projects_POs))
                    .setContentText(list_of_projects_POs)
                    .setContentTitle("Graphing Projects")
                    .setSmallIcon(R.drawable.ic_temp_chart)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true);

            notificationManager.notify(GRAPH_NOTIFICATION_ID, builder.build());
            startForeground(GRAPH_NOTIFICATION_ID,builder.build());
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
