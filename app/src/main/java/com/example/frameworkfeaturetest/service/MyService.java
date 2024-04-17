package com.example.frameworkfeaturetest.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.ActivityManagerTestActivity;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 1;
    private static final String MY_NOTIFICATION_CHANNEL_ID = "my_channel_id";
    private static final String MY_NOTIFICATION_CHANNEL_NAME = "my_channel_name";


    private NotificationManager mNotificationManager;

    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");

        Notification notification = buildNotification();
        
        startForeground(NOTIFICATION_ID, notification);
        
        return START_STICKY;
    }

    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, ActivityManagerTestActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(MY_NOTIFICATION_CHANNEL_ID, MY_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        Notification notification = new Notification.Builder(this, MY_NOTIFICATION_CHANNEL_ID)
                .setContentTitle("我的前台服务")
                .setContentText("我的前台服务正在运行")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        return notification;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
    }
}