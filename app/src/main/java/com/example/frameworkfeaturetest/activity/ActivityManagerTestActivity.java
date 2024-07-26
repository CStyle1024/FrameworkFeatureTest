package com.example.frameworkfeaturetest.activity;

import static com.example.frameworkfeaturetest.receiver.TestReceiver.ACTION_TEST_RECEIVER;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Broadcaster;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;
import com.example.frameworkfeaturetest.service.MyService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ActivityManagerTestActivity extends TestBaseActivity {

    private static final String TAG = ActivityManagerTestActivity.class.getSimpleName();
    private ActivityManager mActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_test);

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initSystemService() {
        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    }


    public void onClick(View view) {
        final int viewId = view.getId();
        if (R.id.btn_start_service == viewId) {
            Intent intent = new Intent(this, MyService.class);
        } else if (R.id.btn_bind_service == viewId) {
            Intent intent = new Intent(this, MyService.class);
            bindService(intent, new MyServiceConnection(), Context.BIND_AUTO_CREATE);
        }
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.d(TAG, "onBindingDied: ");
        }

        @Override
        public void onNullBinding(ComponentName name) {
            Log.d(TAG, "onNullBinding: ");
        }
    }

    private void testStartActivity() {
        Intent intent = new Intent(this, EmptyTestActivity.class);
        startActivity(intent);
    }

    private void startService() {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    private void startForegroundService() {
        Intent intent = new Intent("com.example.frameworkfeaturetest.start_service");
        intent.setPackage(getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void testDiskWrite() {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File mytext = new File(externalStorageDir, "mytext.txt");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mytext);
            fos.write("Test Strict Mode.".getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void testLargeHeap() {
        long maxMem = Runtime.getRuntime().maxMemory() / 1000 / 1000;
        Log.d(TAG, "onCreate: maxMem=" + maxMem + " MB.");
    }
}