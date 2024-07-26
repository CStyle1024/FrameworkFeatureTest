package com.example.frameworkfeaturetest;

import android.app.Application;
import android.os.StrictMode;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()   // or .detectAll() for all detectable problems
//                .penaltyLog()
//                .build());
    }
}
