package com.example.frameworkfeaturetest.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Button;

import androidx.core.app.NotificationCompat;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.NotificationTestActivity;
import com.example.frameworkfeaturetest.activity.PackageManagerTestActivity;

public class NotificationHelper {
    private static final String TAG = NotificationHelper.class.getSimpleName();
    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification() {
        Log.d(TAG, "showNotification: ");
        // 创建 Notification 频道 (仅在 Android 8.0 及以上版本需要)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // 创建 PendingIntent 用于点击通知后跳转到应用界面
        Intent intent = new Intent(context, NotificationTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // 构建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Notification Title")
                .setContentText("Notification Text")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setOngoing(true) ;

        Notification notification = builder.build();

        // 发送通知
        notificationManager.notify(0, notification);
    }
}
