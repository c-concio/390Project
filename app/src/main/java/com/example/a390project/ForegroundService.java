package com.example.a390project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "NotificationChannel";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String taskTitle = intent.getStringExtra("taskTitle");
        final String taskID = intent.getStringExtra("taskID");
        final int NOTIFICATION_ID = intent.getIntExtra("NOTIFICATION_ID",0);
        final String projectPO = intent.getStringExtra("projectPO");
        final long timeNow = intent.getLongExtra("timeNow",0);

        Intent intent2;
        if(taskTitle.equals("Inspection") || taskTitle.equals("Final-Inspection")) {
            intent2 = new Intent(getApplicationContext(), TaskInspectionActivity.class);
            // send the TaskInspectionActivity the projectPO
            intent2.putExtra("inspectionTaskID", taskID);
        }
        else if(taskTitle.equals("Sanding") || taskTitle.equals("Sand-Blasting") || taskTitle.equals("Manual Solvent Cleaning") || taskTitle.equals("Iridite") || taskTitle.equals("Masking")) {
            intent2 = new Intent(getApplicationContext(), TaskPrePaintingActivity.class);
            intent2.putExtra("prepaintingTaskID", taskID);
        }
        else if(taskTitle.equals("Painting")) {
            intent2 = new Intent(getApplicationContext(), TaskPaintingActivity.class);
            intent2.putExtra("paintingTaskID", taskID);
        }
        else if(taskTitle.equals("Baking")) {
            intent2 = new Intent(getApplicationContext(), TaskBakingActivity.class);
            intent2.putExtra("bakingTaskID", taskID);
        }
        else {
            intent2 = new Intent(getApplicationContext(), TaskPackagingActivity.class);
            // send the taskPackagingActivity the projectPO
            intent2.putExtra("packagingTaskID", taskID);
        }

        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), NOTIFICATION_ID, intent2, 0);
        final long timeAtNotificationCreation = System.currentTimeMillis();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                // TODO do your thing

                long timeElapsed = System.currentTimeMillis() - timeAtNotificationCreation;

                long sec = (timeElapsed/1000) % 60;
                long min = ((timeElapsed/1000) / 60) % 60;
                long hour = ((timeElapsed/1000) / 60) / 60;

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setContentTitle("WorkBlock - " + projectPO + " - " + taskTitle)
                        .setSubText("Start Time: " + getDate(timeNow))
                        .setContentText("Time Elapsed: " + hour+":"+min+":"+sec)
                        .setSmallIcon(R.drawable.ic_work_block)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notification);
                startForeground(NOTIFICATION_ID,notification);

            }
        }, 0, 1000);
        return START_NOT_STICKY;
    }

    private String getDate(long time) {
        String value;

        if(time == 0) {
            value = "-";
        }
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a dd-MM-yyyy");
            value = formatter.format(time);
        }

        return value;
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
