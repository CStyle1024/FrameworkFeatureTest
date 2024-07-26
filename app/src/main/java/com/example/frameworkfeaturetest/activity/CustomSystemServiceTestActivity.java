package com.example.frameworkfeaturetest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomSystemServiceTestActivity extends TestBaseActivity {

    private static final String TAG = CustomSystemServiceTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_system_service_test);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initSystemService() {

    }

    public void onClick(View v) {
        testBeautifulGirl();
    }

    private void testBeautifulGirl() {
        try {
            Class<?> bgClass = Class.forName("flyme.beautiful.BeautifulGirlManager");
            Method getInstanceM = bgClass.getMethod("getInstance");
            Object bgInstance = getInstanceM.invoke(null);

            Method receiverLoveLetterM = bgClass.getMethod("receiverLoveLetter", String.class);
            Object result = receiverLoveLetterM.invoke(bgInstance, "Hello");
            Log.d(TAG, "testBeautifulGirl: result=" + result);

            Method sendMyBinderM = bgClass.getMethod("sendMyBinder");
            Log.d(TAG, "testBeautifulGirl: sendMyBinderM=" + sendMyBinderM);
            sendMyBinderM.invoke(bgInstance);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            Log.e(TAG, "testBeautifulGirl: ", e);
        }
    }
}