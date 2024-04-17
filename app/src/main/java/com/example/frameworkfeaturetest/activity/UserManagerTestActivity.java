package com.example.frameworkfeaturetest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;

import java.io.IOException;

public class UserManagerTestActivity extends TestBaseActivity {

    private static final String TAG = UserManagerTestActivity.class.getSimpleName();

    private UserManager mUserManager;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: activity_user_manager_test");
        setContentView(R.layout.activity_user_manager_test);

        initViews();
    }

    @Override
    public void initViews() {
        Log.d(TAG, "initViews: ");
        mImageView = (ImageView) findViewById(R.id.iv_icon);
    }

    @Override
    public void initSystemService() {
        mUserManager = getSystemService(UserManager.class);
    }

    public void onClick(View view) {
        int viewId = view.getId();
        if (R.id.btn_get == viewId) {
            getUserIcon();
        } else if (R.id.btn_set == viewId) {
            setUserIcon();
        }
    }

    private void setUserIcon() {
        Bitmap photoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        mUserManager.setUserIcon(photoBitmap);
    }

    private void getUserIcon() {
        Bitmap photoBitmap = mUserManager.getUserIcon();
        Log.d(TAG, "getUserIcon: result=" + photoBitmap);
        BitmapDrawable photoDrawable = new BitmapDrawable(photoBitmap);
        mImageView.setImageDrawable(photoDrawable);
        new IOException().printStackTrace();
    }
}