package com.example.frameworkfeaturetest.widget;

import static com.example.frameworkfeaturetest.widget.Constants.DEBUG_WIDGET;
import static com.example.frameworkfeaturetest.widget.Constants.TAG_WIDEGT;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.frameworkfeaturetest.R;

import java.util.Arrays;
import java.util.Map;

public class MyAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = DEBUG_WIDGET ? "MyAppWidgetProvider" : TAG_WIDEGT;

    public static final String TOAST_ACTION = "com.example.frameworkfeaturetest.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.frameworkfeaturetest.EXTRA_ITEM";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: action=" + intent.getAction());
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate: appWidgetIds=" + Arrays.toString(appWidgetIds));

        for (int i = 0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, MyWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout /*layoutId*/);
//            views.setRemoteAdapter(appWidgetId, R.id.stack_view, intent); // SetRemoteViewsAdapterIntent
            views.setRemoteAdapter(R.id.stack_view, intent); // SetRemoteViewsAdapterIntent

            views.setEmptyView(R.id.stack_view, R.id.empty_view);

            Intent toastIntent = new Intent(context, MyAppWidgetProvider.class);
            toastIntent.setAction(MyAppWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            // viewId
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews createMyRemoteViews(Context context) {
        Intent intent = new Intent(context, MyAppWidgetConfigurationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_appwidget_layout);
        remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);

        return remoteViews;
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.d(TAG, "onAppWidgetOptionsChanged: ");
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled: ");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted: appWidgetIds=" + Arrays.toString(appWidgetIds));
    }
}
