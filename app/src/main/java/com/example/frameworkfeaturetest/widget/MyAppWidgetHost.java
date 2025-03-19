package com.example.frameworkfeaturetest.widget;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

public class MyAppWidgetHost extends AppWidgetHost {

    public MyAppWidgetHost(Context context, int hostId) {
        super(context, hostId);
    }

    @Override
    protected void onProviderChanged(int appWidgetId, AppWidgetProviderInfo appWidget) {
        super.onProviderChanged(appWidgetId, appWidget);
    }
}
