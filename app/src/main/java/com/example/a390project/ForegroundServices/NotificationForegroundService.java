package com.example.a390project.ForegroundServices;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.a390project.R;
import com.example.a390project.TaskBakingActivity;
import com.example.a390project.TaskInspectionActivity;
import com.example.a390project.TaskPackagingActivity;
import com.example.a390project.TaskPaintingActivity;
import com.example.a390project.TaskPrePaintingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationForegroundService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "NotificationChannel";
    private static final String TAG = "WorkBlockFS";

    //variables from Intent
    private int NOTIFICATION_ID = 0;
    private String taskTitle;
    private String taskID;
    private String projectPO;
    private long timeNow;


    private NotificationCompat.Builder builder = null;

    @Override
    public void onCreate() {
        builder = null;
        NOTIFICATION_ID = 0;
        super.onCreate();
        Thread t = new Thread(new Runnable() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                while(true) {
                    if(builder != null && NOTIFICATION_ID !=0) {
                        startForeground(NOTIFICATION_ID, builder.build());
                        Log.d(TAG, "run: FOREGROUND STARTED");
                        break;
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        taskTitle = intent.getStringExtra("taskTitle");
        taskID = intent.getStringExtra("taskID");
        NOTIFICATION_ID = intent.getIntExtra("NOTIFICATION_ID",0);
        projectPO = intent.getStringExtra("projectPO");
        timeNow = intent.getLongExtra("timeNow",0);

        Intent intent2;
        if(taskTitle.equals("Inspection") || taskTitle.equals("Final-Inspection")) {
            intent2 = new Intent(getApplicationContext(), TaskInspectionActivity.class);
            // send the TaskInspectionActivity the projectPO
            intent2.putExtra("inspectionTaskID", taskID);
        }
        else if(taskTitle.equals("Sanding") || taskTitle.equals("SandBlasting") || taskTitle.equals("ManualSolventCleaning") || taskTitle.equals("Iridite") || taskTitle.equals("Masking")) {
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
        final NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final long timeAtNotificationCreation = System.currentTimeMillis();

        builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(taskTitle + " - " + projectPO)
                .setSubText("Start Time: " + getDate(timeNow))
                .setSmallIcon(R.drawable.ic_work_block)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setOngoing(true);

        final TimerTask[] timerTask = new TimerTask[1];
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                timerTask[0] = new TimerTask() {
                    @Override
                    public void run() {
                        long timeElapsed = System.currentTimeMillis() - timeAtNotificationCreation;

                        long sec = (timeElapsed/1000) % 60;
                        long min = ((timeElapsed/1000) / 60) % 60;
                        long hour = ((timeElapsed/1000) / 60) / 60;

                        String s,m,h;
                        if (sec < 10)
                            s = "0" + Long.toString(sec);
                        else
                            s = Long.toString(sec);
                        if (min < 10)
                            m = "0" + Long.toString(min);
                        else
                            m = Long.toString(min);
                        if (hour < 10)
                            h = "0" + Long.toString(hour);
                        else
                            h = Long.toString(hour);

                        String elapsedTime = h + ":" + m + ":" + s;

                        taskTitle = intent.getStringExtra("taskTitle");
                        taskID = intent.getStringExtra("taskID");
                        NOTIFICATION_ID = intent.getIntExtra("NOTIFICATION_ID",0);
                        projectPO = intent.getStringExtra("projectPO");
                        timeNow = intent.getLongExtra("timeNow",0);

                        builder.setDefaults(0);
                        builder.setContentText("Time Elapsed: " + elapsedTime)
                                .setContentTitle(taskTitle + " - " + projectPO)
                                .setSubText("Start Time: " + getDate(timeNow))
                                .setSmallIcon(R.drawable.ic_work_block)
                                .setContentIntent(pendingIntent)
                                .setVibrate(new long[]{0L})
                                .setOngoing(true);

                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                        //startForeground(NOTIFICATION_ID,builder.build());
                        Log.d(TAG, "updateNotification: " + taskTitle);

                    }
                };
                Timer timer = new Timer(NOTIFICATION_ID+"");
                timer.scheduleAtFixedRate(timerTask[0], 0, 1000);
            }
        });


        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final String uId = FirebaseAuth.getInstance().getUid();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                rootRef.child("users").child(uId).child("workingTasks").child(taskID).child("canEnd").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean canEnd = dataSnapshot.getValue(boolean.class);
                        if (!canEnd) {
                            long idLong = (timeNow % 10000000);
                            int NOTIFICATION_ID = (int)idLong;
                            timerTask[0].cancel();
                            notificationManager.cancel(NOTIFICATION_ID);
                            Log.d(TAG, "removeNotification: " + NOTIFICATION_ID);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        t.start();
        t2.start();

        return START_STICKY;
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
