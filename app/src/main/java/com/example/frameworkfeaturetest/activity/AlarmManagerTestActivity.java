package com.example.frameworkfeaturetest.activity;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;

public class AlarmManagerTestActivity extends TestBaseActivity {

    private static final String TAG = "AlarmManagerTestActivity";

    private AlarmManager mAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager_test);

        testAlarm(this);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initSystemService() {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    private void testAlarm(Context context) {
        Log.d(TAG, "testAlarm: ");
        Intent intent = new Intent("com.flyme.auto.usagestatsreporter.action.USAGESTATS_REPORT_HEARTBEAT");
        PendingIntent heatBeatIntent = PendingIntent.getBroadcast(context,
                0x1, intent, FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, heatBeatIntent);
    }

}