package com.example.frameworkfeaturetest.minormode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MinorModeVerificationActivity extends TestBaseActivity {
    private static final String TAG = "MinorModeVerification";
    private int mSessionId = -1;

    private int mActivityResultCode = Activity.RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minor_mode_verification);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        int sessionId = intent.getIntExtra(PackageInstaller.EXTRA_SESSION_ID, -1);
        Log.d(TAG, "onCreate: sessionId: " + sessionId);
        mSessionId = sessionId;

        initViews();
    }

    @Override
    public void initViews() {
        findViewById(R.id.btn_allow_install).setOnClickListener(v -> {
            setActivityResult(Activity.RESULT_OK);
            finish();
        });
        findViewById(R.id.btn_deny_install).setOnClickListener(v -> {
            setActivityResult(Activity.RESULT_CANCELED);
            finish();
        });
    }

    @Override
    public void initSystemService() {

    }

    private void setActivityResult(int resultCode) {
        mActivityResultCode = resultCode;
        super.setResult(resultCode);
    }

    @Override
    public void finish() {
        if (mSessionId != -1) {
            if (mActivityResultCode == Activity.RESULT_OK) {
                setPermissionsResult(mSessionId, true);
            } else {
                setPermissionsResult(mSessionId, false);
            }
        }
        super.finish();
    }

    public void setPermissionsResult(int sessionId, boolean accept) {
        PackageInstaller packageInstaller = getPackageManager().getPackageInstaller();
        try {
            Class<?> packageInstallerClass = Class.forName("android.content.pm.PackageInstaller");
            Method setPermissionsResultM = packageInstallerClass.getMethod("setPermissionsResult", int.class, boolean.class);
            setPermissionsResultM.invoke(packageInstaller, sessionId, accept);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}