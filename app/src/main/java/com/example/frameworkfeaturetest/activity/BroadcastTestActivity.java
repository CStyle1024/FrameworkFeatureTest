package com.example.frameworkfeaturetest.activity;

import static com.example.frameworkfeaturetest.receiver.TestReceiver.ACTION_TEST_RECEIVER;

import androidx.annotation.NonNull;
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
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.provider.Settings;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;
import com.example.frameworkfeaturetest.receiver.TestReceiver;
import com.example.frameworkfeaturetest.receiver.TestReceiver2;
import com.example.frameworkfeaturetest.receiver.TestReceiver3;
import com.example.frameworkfeaturetest.util.NotificationHelper;

import java.util.ArrayList;
import java.util.Set;

public class BroadcastTestActivity extends TestBaseActivity {

    public static final String TAG = BroadcastTestActivity.class.getSimpleName();

    private Handler mHandler;
    private Handler mHandler2;
    private HandlerThread mReceiveThread;
    private HandlerThread mReceiveThread2;

    private TestReceiver mReceiver = new TestReceiver();
    private TestReceiver2 mReceiver2 = new TestReceiver2();
    private TestReceiver3 mReceiver3 = new TestReceiver3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braodcast_test);

        mReceiveThread = new HandlerThread("ReceiverThread");
        mReceiveThread.start();
        mHandler = new Handler(mReceiveThread.getLooper());

        mReceiveThread2 = new HandlerThread("ReceiverThread2");
        mReceiveThread2.start();
        mHandler2 = new Handler(mReceiveThread2.getLooper());

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
//            sendBroadcast();
            testNonNull(null);
        }
    }

    private void sendBroadcast() {
        Log.d(TAG, "sendBroadcast: ");
        int i = 0;
//        while(i < 500) {
            Intent intent = new Intent(ACTION_TEST_RECEIVER);
//        intent.setPackage(getPackageName());
        sendOrderedBroadcast(intent, null);
//            sendBroadcast(intent, null);
//        }
    }

    private void registerDynamicReceiver() {
        IntentFilter intentFilter1 = new IntentFilter(ACTION_TEST_RECEIVER);
        intentFilter1.setPriority(800);
//        registerReceiver(testReceiver, intentFilter1);
        registerReceiver(mReceiver, intentFilter1, null, mHandler);


//        IntentFilter intentFilter2 = new IntentFilter(ACTION_TEST_RECEIVER);
//        intentFilter2.addAction(Intent.ACTION_PACKAGE_CHANGED);
//        intentFilter2.setPriority(800);
//        registerReceiver(mReceiver2, intentFilter2);
//        registerReceiver(mReceiver2, intentFilter2, null, mHandler2);

//        IntentFilter intentFilter3 = new IntentFilter(Intent.ACTION_PACKAGE_RESTARTED);
//        intentFilter3.addDataScheme("package");
//        registerReceiver(mReceiver3, intentFilter3);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @NonNull
    public String testNonNull(@NonNull BroadcastReceiver receiver) {
        return receiver.toString();
    }
}