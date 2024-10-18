package com.example.frameworkfeaturetest.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;
import com.example.frameworkfeaturetest.receiver.MyDeviceAdminReceiver;

public class DevicePolicyManagerTestActivity extends TestBaseActivity {

    private static final String TAG = "DevicePolicyManagerTestActivity";

    private static final int REQUEST_CODE_ENABLE_LOCK_TASK = 0x1;
    private DevicePolicyManager mDpm;
    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_policy_manager_test);

        init(this);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initSystemService() {
        mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    private void init(Context context) {
        mComponentName = new ComponentName(context, MyDeviceAdminReceiver.class);
        checkLockTaskPermission(context);
    }

    public void onClick(View view) {
        int viewId = view.getId();
        if (R.id.btn_lock_task == viewId) {
            Log.d(TAG, "onClick: btn_lock_task");
            mDpm.setLockTaskPackages(mComponentName, new String[] { this.getPackageName() });
            boolean hasLockTaskPermission = mDpm.isLockTaskPermitted(this.getPackageName());
            Log.d(TAG, "checkLockTaskPermission: hasLockTaskPermission:" + hasLockTaskPermission);
            startLockTask();
        } else if (R.id.btn_unlock_task == viewId) {
            Log.d(TAG, "onClick: btn_unlock_task");
            stopLockTask();
        } else if (R.id.btn_hide_pkg == viewId) {
            Log.d(TAG, "onClick: btn_hide_pkg");
            test();
        }
    }

    private void checkLockTaskPermission(Context context) {
        boolean hasLockTaskPermission = mDpm.isLockTaskPermitted(this.getPackageName());
        Log.d(TAG, "checkLockTaskPermission: hasLockTaskPermission:" + hasLockTaskPermission);
        if (!hasLockTaskPermission) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Explanation for lock task permission");
            startActivityForResult(intent, REQUEST_CODE_ENABLE_LOCK_TASK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_ENABLE_LOCK_TASK == requestCode) {
            if (RESULT_OK == resultCode) {
                Log.d(TAG, "onActivityResult: request success.");

            } else {
                Log.d(TAG, "onActivityResult: request failed.");
            }
        }
    }

    public void enableAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable device admin to control packages.");
        startActivity(intent);
    }

    private void test() {
        boolean adminActive = mDpm.isAdminActive(mComponentName);
        Log.d(TAG, "test: adminActive=" + adminActive);
        if (adminActive) {
            mDpm.setApplicationHidden(mComponentName, "tv.danmaku.bili", true);
        } else {
            Log.d(TAG, "test: ");
        }
    }
}