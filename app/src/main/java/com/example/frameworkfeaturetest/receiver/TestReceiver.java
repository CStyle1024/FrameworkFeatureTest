package com.example.frameworkfeaturetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestReceiver extends BroadcastReceiver {
    public static final String TAG = TestReceiver.class.getSimpleName();
    public static final String ACTION_TEST_RECEIVER = "com.example.frameworkfeaturetest.action.ACTION_TEST_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
    }
}
