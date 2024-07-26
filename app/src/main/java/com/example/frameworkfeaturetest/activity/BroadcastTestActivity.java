package com.example.frameworkfeaturetest.activity;

import static com.example.frameworkfeaturetest.receiver.TestReceiver.ACTION_TEST_RECEIVER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.receiver.TestReceiver;
import com.example.frameworkfeaturetest.receiver.TestReceiver2;

public class BroadcastTestActivity extends AppCompatActivity {

    public static final String TAG = BroadcastTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braodcast_test);

        registerDynamicReceiver();
    }

    public void onClick(View view) {
        int viewId = view.getId();
        if (R.id.btn_send_br == viewId) {
            sendOrderedBroadcast();
        }
    }

    private void sendOrderedBroadcast() {
        Log.d(TAG, "sendBroadcast: ");
        Intent intent = new Intent(ACTION_TEST_RECEIVER);
//        intent.setPackage(getPackageName());
        sendOrderedBroadcast(intent, null);
    }

    private void registerDynamicReceiver() {
        IntentFilter intentFilter1 = new IntentFilter(ACTION_TEST_RECEIVER);
        intentFilter1.setPriority(1000);
        TestReceiver testReceiver = new TestReceiver();

        IntentFilter intentFilter2 = new IntentFilter(ACTION_TEST_RECEIVER);
        TestReceiver2 testReceiver2 = new TestReceiver2();
        registerReceiver(testReceiver, intentFilter1);
        registerReceiver(testReceiver2, intentFilter2);
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