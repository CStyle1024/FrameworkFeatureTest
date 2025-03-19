package com.example.frameworkfeaturetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.util.Log;

public class MinorModeReceiver extends BroadcastReceiver {

    private static final String TAG = "MinorModeVerification";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: intent=" + intent);

        int sessionId = intent.getIntExtra(PackageInstaller.EXTRA_SESSION_ID, -1);
        if (sessionId != -1) {
            Intent parcelableExtra = intent.getParcelableExtra(Intent.EXTRA_INTENT);
            try {
                context.startActivity(parcelableExtra);
            } catch (Exception e) {
                Log.e(TAG, "onReceive: ", e);
                try {
                    parcelableExtra.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(parcelableExtra);
                } catch (Exception e1) {
                    Log.e(TAG, "onReceive: ", e1);
                    e1.printStackTrace();
                }
            }
        }
    }
}
