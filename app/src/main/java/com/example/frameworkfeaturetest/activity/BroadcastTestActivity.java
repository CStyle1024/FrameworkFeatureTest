package com.example.frameworkfeaturetest.activity;

import static com.example.frameworkfeaturetest.receiver.TestReceiver.ACTION_TEST_RECEIVER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.example.frameworkfeaturetest.R;

public class BroadcastTestActivity extends AppCompatActivity {

    public static final String TAG = BroadcastTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braodcast_test);
    }

    private void sendBroadcast() {
        Log.d(TAG, "sendBroadcast: ");
        Intent intent = new Intent(ACTION_TEST_RECEIVER);
        sendBroadcast(intent);
    }

    private void registerReceiver() {
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