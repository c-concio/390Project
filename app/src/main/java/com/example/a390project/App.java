package com.example.a390project;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String NOTIFICATION_CHANNEL_ID = "NotificationChannel";
    public static final String GRAPH_CHANNEL_ID = "GraphChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel(GRAPH_CHANNEL_ID, "Graph Service Channel");
        createNotificationChannel(NOTIFICATION_CHANNEL_ID,"WorkBlock Service Channel");
    }

    private void createNotificationChannel(String channelID, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    channelID,
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT
            );


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
