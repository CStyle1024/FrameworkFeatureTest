package com.example.frameworkfeaturetest.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.provider.MzSettings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.ActivityManagerTestActivity;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 1;
    private static final String MY_NOTIFICATION_CHANNEL_ID = "my_channel_id";
    private static final String MY_NOTIFICATION_CHANNEL_NAME = "my_channel_name";

    private NotificationManager mNotificationManager;
    private boolean mForegroundService = false;

    private static final Uri MZ_BATCH_APP_HIDDEN_CHANGED = Settings.Global
            .getUriFor(MzSettings.Global.MZ_BATCH_APP_HIDDEN_CHANGED);
    private static final Uri PRIVATE_MODE_RUNNING = Settings.Global
            .getUriFor(MzSettings.Global.PRIVATE_MODE_RUNNING);
    private ContentObserver mContentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, @Nullable Uri uri) {
            super.onChange(selfChange, uri);
            if (uri.equals(MZ_BATCH_APP_HIDDEN_CHANGED)) {
                boolean hidden = Settings.Global.getInt(getContentResolver(), MzSettings.Global.MZ_BATCH_APP_HIDDEN_CHANGED, 0) == 1;
                Log.d(TAG, "onChange: uri=" + uri + ", hidden=" + hidden);
            } else if (uri.equals(PRIVATE_MODE_RUNNING)) {
                boolean running = Settings.Global.getInt(getContentResolver(), MzSettings.Global.PRIVATE_MODE_RUNNING, 0) == 1;
                Log.d(TAG, "onChange: uri=" + uri + ", running=" + running);
            }
        }
    };

    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        setForegroundService(true);
        getContentResolver().registerContentObserver(
                    MZ_BATCH_APP_HIDDEN_CHANGED,
                    false, mContentObserver);
        getContentResolver()
                .registerContentObserver(
                        PRIVATE_MODE_RUNNING,
                        false, mContentObserver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind: ");
        super.onRebind(intent);
    }

    /**
     * 已经不会执行
     *
     * @param intent
     * @param startId
     */
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        setForegroundService(true);

        return START_STICKY;
    }

    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, ActivityManagerTestActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        ;

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

    private void setForegroundService(boolean foreground) {
        Log.d(TAG, "setForegroundService: ");
        if (mForegroundService != foreground) {
            mForegroundService = foreground;
            if (foreground) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        MY_NOTIFICATION_CHANNEL_ID,
                        MY_NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_LOW);
                Notification.Builder notificationBuilder =
                        new Notification.Builder(getApplicationContext(), MY_NOTIFICATION_CHANNEL_ID);
                notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);

                NotificationManager notificationManager =
                        (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
                startForeground(-999, notificationBuilder.build());
            } else {
                stopForeground(true);
            }
        }
    }

    public static void startMyselfForegroundIfNeeded(@NonNull Context c) {
        Intent intent = new Intent(c, MyService.class);
        c.startForegroundService(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
    }
}