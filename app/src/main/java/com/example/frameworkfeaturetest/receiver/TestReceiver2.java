package com.example.frameworkfeaturetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TestReceiver2 extends BroadcastReceiver {
//    public static final String TAG = TestReceiver2.class.getSimpleName();
    public static final String TAG = "BroadcastTestActivity";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: TestReceiver2");
    }
}
