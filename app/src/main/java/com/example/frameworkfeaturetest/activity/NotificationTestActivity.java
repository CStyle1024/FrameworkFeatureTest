package com.example.frameworkfeaturetest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.os.TraceKt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;
import com.example.frameworkfeaturetest.util.NotificationHelper;

public class NotificationTestActivity extends TestBaseActivity {

    private static final String TAG = NotificationTestActivity.class.getSimpleName();

    private NotificationHelper mNotificationHelper;
    private Button mBtnAllowNoti;
    private Button mBtnSendNoti;
    private View.OnClickListener mOnClickListener;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        initViews();

        mHandler = new Handler(Looper.myLooper());
        mNotificationHelper = new NotificationHelper(NotificationTestActivity.this);
    }

    @Override
    public void initViews() {
        mOnClickListener = v -> {
            int viewId = v.getId();
            if (R.id.btn_allow_noti == viewId) {
                requestNotificationPermission(NotificationTestActivity.this);
            } else {
                mNotificationHelper.showNotification();
                Debug.startMethodTracing("sample");
                mHandler.post(() -> {
                        try {
                            Log.d(TAG, "initViews: ");
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                Debug.stopMethodTracing();
            }
        };

        mBtnAllowNoti = findViewById(R.id.btn_allow_noti);
        mBtnAllowNoti.setOnClickListener(mOnClickListener);
        mBtnSendNoti = findViewById(R.id.btn_send_noti);
        mBtnSendNoti.setOnClickListener(mOnClickListener);

    }

    @Override
    public void initSystemService() {

    }

    public static final String POST_NOTIFICATIONS="android.permission.POST_NOTIFICATIONS";
    public void requestNotificationPermission(Activity activity) {

        if (Build.VERSION.SDK_INT >= 33) {
            if (ActivityCompat.checkSelfPermission(activity, POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale( activity, POST_NOTIFICATIONS)) {
                    enableNotification(activity);
                }else{
                    ActivityCompat.requestPermissions( activity,new String[]{POST_NOTIFICATIONS},100);
                }
            }
        } else {
            boolean enabled = NotificationManagerCompat.from(activity).areNotificationsEnabled();
            if (!enabled) {
                enableNotification(activity);
            }
        }
    }

    public static void enableNotification(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE,context. getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",context. getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }
    }
}