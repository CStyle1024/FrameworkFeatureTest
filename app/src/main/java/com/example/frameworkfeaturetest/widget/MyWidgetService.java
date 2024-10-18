package com.example.frameworkfeaturetest.widget;

import static com.example.frameworkfeaturetest.widget.Constants.DEBUG_WIDGET;
import static com.example.frameworkfeaturetest.widget.Constants.TAG_WIDEGT;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViewsService;

public class MyWidgetService extends RemoteViewsService {
    private static final String TAG = DEBUG_WIDGET ?  "MyWidgetService" : TAG_WIDEGT;

    public MyWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return super.onBind(intent);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyViewsFactory(this.getApplicationContext(), intent);
    }
}