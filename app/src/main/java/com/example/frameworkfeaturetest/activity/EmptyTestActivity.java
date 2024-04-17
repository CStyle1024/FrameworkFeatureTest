package com.example.frameworkfeaturetest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.frameworkfeaturetest.R;

public class EmptyTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_test);
    }
}