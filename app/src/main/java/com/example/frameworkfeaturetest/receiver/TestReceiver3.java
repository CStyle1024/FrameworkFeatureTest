package com.example.frameworkfeaturetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class TestReceiver3 extends BroadcastReceiver {
    
    private static final String TAG = TestReceiver3.class.getSimpleName();
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        /*Uri uri = intent.getData();
        String packageName = uri.getSchemeSpecificPart();
        Log.d(TAG, "onReceive: package " + packageName);*/
    }
}
