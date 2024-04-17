package com.example.frameworkfeaturetest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;

public class AppWidgetServiceTestActivity extends TestBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_widget_service_test);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initSystemService() {

    }
}