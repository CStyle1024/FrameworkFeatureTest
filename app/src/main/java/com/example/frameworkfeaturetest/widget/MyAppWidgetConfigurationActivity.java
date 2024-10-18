package com.example.frameworkfeaturetest.widget;

import static com.example.frameworkfeaturetest.widget.Constants.DEBUG_WIDGET;
import static com.example.frameworkfeaturetest.widget.Constants.TAG_WIDEGT;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.example.frameworkfeaturetest.R;

public class MyAppWidgetConfigurationActivity extends AppCompatActivity {

    private static final String TAG = DEBUG_WIDGET ?  "MyViewsFactory" : TAG_WIDEGT;

    private Button mBtnNotifyDataChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app_widget_configuration);

        initViews(this);
    }

    private void initViews(Context context) {
        mBtnNotifyDataChanged = findViewById(R.id.notify_data_changed);
    }
}