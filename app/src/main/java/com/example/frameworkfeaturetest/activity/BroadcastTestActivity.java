package com.example.frameworkfeaturetest.activity;

import static com.example.frameworkfeaturetest.receiver.TestReceiver.ACTION_TEST_RECEIVER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.provider.Settings;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;
import com.example.frameworkfeaturetest.receiver.TestReceiver;
import com.example.frameworkfeaturetest.receiver.TestReceiver2;
import com.example.frameworkfeaturetest.util.NotificationHelper;

public class BroadcastTestActivity extends TestBaseActivity {

    public static final String TAG = BroadcastTestActivity.class.getSimpleName();

    private Handler mHandler;
    private HandlerThread mReceiveThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braodcast_test);

        mReceiveThread = new HandlerThread("ReceiverThread");
        mReceiveThread.start();
        mHandler = new Handler(mReceiveThread.getLooper());

        registerDynamicReceiver();
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initSystemService() {

    }

    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
        int viewId = view.getId();
        if (R.id.btn_send_br == viewId) {
            sendBroadcast();
        }
    }

    private void sendBroadcast() {
        Log.d(TAG, "sendBroadcast: ");
        Intent intent = new Intent(ACTION_TEST_RECEIVER);
//        intent.setPackage(getPackageName());
//        sendOrderedBroadcast(intent, null);
        sendBroadcast(intent, null);
    }

    private void registerDynamicReceiver() {
        IntentFilter intentFilter1 = new IntentFilter(ACTION_TEST_RECEIVER);
        intentFilter1.setPriority(1000);
        TestReceiver testReceiver = new TestReceiver();
        registerReceiver(testReceiver, intentFilter1);


        IntentFilter intentFilter2 = new IntentFilter(ACTION_TEST_RECEIVER);
        intentFilter2.addAction(Intent.ACTION_PACKAGE_CHANGED);
        TestReceiver2 testReceiver2 = new TestReceiver2();
        registerReceiver(testReceiver2, intentFilter2, null, mHandler);
    }

    private void registerPackageChangedReceiver() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_CHANGED);
        MyBroadcastReceiver mbr = new MyBroadcastReceiver();
        intentFilter.setPriority(1000);
        intentFilter.addDataScheme("package");
        registerReceiver(mbr, intentFilter);
    }

    static class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: action=" + intent.getAction());
        }
    }
}