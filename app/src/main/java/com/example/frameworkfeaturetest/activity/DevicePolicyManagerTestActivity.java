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
    private DevicePolicyManager mDevicePolicyManager;
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
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    private void init(Context context) {
        mComponentName = new ComponentName(context, MyDeviceAdminReceiver.class);
        checkLockTaskPermission(context);
    }

    public void onClick(View view) {
        int viewId = view.getId();
        if (R.id.btn_lock_task == viewId) {
            Log.d(TAG, "onClick: btn_lock_task");
            mDevicePolicyManager.setLockTaskPackages(mComponentName, new String[] { this.getPackageName() });
            boolean hasLockTaskPermission = mDevicePolicyManager.isLockTaskPermitted(this.getPackageName());
            Log.d(TAG, "checkLockTaskPermission: hasLockTaskPermission:" + hasLockTaskPermission);
            startLockTask();
        } else if (R.id.btn_unlock_task == viewId) {
            Log.d(TAG, "onClick: btn_unlock_task");
            stopLockTask();
        }
    }

    private void checkLockTaskPermission(Context context) {
        boolean hasLockTaskPermission = mDevicePolicyManager.isLockTaskPermitted(this.getPackageName());
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
}