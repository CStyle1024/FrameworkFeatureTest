package com.example.frameworkfeaturetest.activity.base;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.frameworkfeaturetest.R;

public abstract class TestBaseActivity extends AppCompatActivity {

    private static final String TAG = TestBaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: activity_test_base");
        setContentView(R.layout.activity_test_base);

        initSystemService();
    }

    public abstract void initViews();

    public abstract void initSystemService();
}