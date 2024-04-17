package com.example.frameworkfeaturetest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;

import com.example.frameworkfeaturetest.R;

public class ResourcesTestActivity extends TestBaseActivity {
    public static final String TAG = ResourcesTestActivity.class.getSimpleName();

    private ImageView mImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_test);
    }

    @Override
    public void initViews() {
        mImageView = (ImageView) findViewById(R.id.showIcon);
    }

    @Override
    public void initSystemService() {

    }

    public void onClick(View view) {
        testGetSystemRes();
    }

    public void testColor() {
        int color = getColor(R.color.testColor);
        String colorHex = Integer.toHexString(color);
        Log.d(TAG, "testColor: colorHex = " + colorHex);
        mImageView.setBackgroundColor(color);
    }

    public void testGetSystemRes() {
        String origPkgName = "com.flyme.pps";
        String testResStr = pkgName2ResName(origPkgName);
        Log.d(TAG, "testSystemRes: resStr=" + testResStr);

        Resources systemRes = getResources().getSystem();

        int resId = com.android.internal.R.string.fingerprint_error_timeout;
        String resStr = systemRes.getString(resId);
        Log.d(TAG, "testSystemRes: resIdStr=" + resStr);

        int sysResId = getResources().getIdentifier("com_android_providers_calendar_enhanced_detail", "string", /*"android"*/null);
        Log.d(TAG, "testSystemRes: resId=" + sysResId);

        String sysResStr = getResources().getString(sysResId);
        Log.d(TAG, "testSystemRes: resStr=" + sysResStr);

//        TypedValue typedValue = new TypedValue();
//        getResources().getValue("example_appwidget_description", typedValue, true);
//        String result = typedValue.string.toString();
//        Log.d(TAG, "testSystemRes: result=" + result);
    }

    private String pkgName2ResName(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException("packageName can't be empty!");
        }

        String testResStr;
        String descPrefix = "flyme_enhanced_";
        String transferName = packageName.replace('.', '_');
        testResStr = descPrefix + transferName;

        return testResStr;
    }
}