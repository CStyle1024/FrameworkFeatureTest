package com.example.frameworkfeaturetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import com.example.frameworkfeaturetest.adapter.MainTestListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayMap<String, Class> mMainTestDataMap;
    private RecyclerView mMainTestRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    private void initView() {
        initMainTestData();
        MainTestListAdapter mtAdapter = new MainTestListAdapter(this, mMainTestDataMap);
        mMainTestRecyclerView = findViewById(R.id.main_test_recycler_view);
        mMainTestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainTestRecyclerView.setAdapter(mtAdapter);
    }

    private void initMainTestData() {
        String[] main_test_array = getResources().getStringArray(R.array.test_activity_array);
        String[] main_test_classes_array = getResources().getStringArray(R.array.test_activity_class_array);
        if (main_test_array == null || main_test_classes_array == null || main_test_array.length != main_test_classes_array.length) {
            throw new IllegalStateException("main_test_array and main_test_classes_array should not be null, and the length both of them must be equal.");
        }

        mMainTestDataMap = new ArrayMap<>();
        for (int i = 0; i < main_test_array.length; i++) {
            Log.d(TAG, "initMainTestData: title=" + main_test_array[i]);
            Class<?> testClass;
            try {
                testClass = Class.forName(main_test_classes_array[i]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            mMainTestDataMap.put(main_test_array[i], testClass);
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
//        if (R.id.btn_pms_test == id) {
//            startActivity(new Intent(this, PackageManagerTestActivity.class));
//        }
    }
}