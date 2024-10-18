package com.example.frameworkfeaturetest.widget;

import static com.example.frameworkfeaturetest.widget.Constants.DEBUG_WIDGET;
import static com.example.frameworkfeaturetest.widget.Constants.TAG_WIDEGT;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.frameworkfeaturetest.R;

import java.util.ArrayList;
import java.util.List;

public class MyViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = DEBUG_WIDGET ?  "MyViewsFactory" : TAG_WIDEGT;

    private static final int mCount = 10;
    private List<WidgetItem> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mWidgetId;

    public MyViewsFactory(Context context, Intent intent) {
        this.mContext = context;
        this.mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(TAG, "MyViewsFactory: mWidgetId=" + mWidgetId);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        for (int i = 0; i < mCount; i++) {
            mWidgetItems.add(new WidgetItem(i + "!"));
        }

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged: ");

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        mWidgetItems.clear();
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: ");
        return mCount;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt: ");
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        views.setTextViewText(R.id.widget_item, mWidgetItems.get(position).text);

        Bundle extras = new Bundle();
        extras.putInt(MyAppWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

//        try {
//            Log.d(TAG, "getViewAt: Loading view " + position);
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.d(TAG, "getLoadingView: ");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d(TAG, "getViewTypeCount: ");
        return 1;
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId: ");
        return position;
    }

    @Override
    public boolean hasStableIds() {
        Log.d(TAG, "hasStableIds: ");
        return false;
    }
}
