package com.example.frameworkfeaturetest.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;

import com.example.frameworkfeaturetest.R;
import com.example.frameworkfeaturetest.activity.base.TestBaseActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import dalvik.system.DexClassLoader;

public class PackageManagerTestActivity extends TestBaseActivity {
    public static final String TAG = PackageManagerTestActivity.class.getSimpleName();
    private PackageManager mPackageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager_test);

        initViews();
        initSystemService();

        Log.d(TAG, "onCreate: cl=" + getClassLoader());
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initSystemService() {
        mPackageManager = getPackageManager();
    }

    public void onClick(View view) {
        String packageName = "com.meizu.alphame";

        int id = view.getId();
        if (R.id.btn_disabled == id) {
            setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
        } else if (R.id.btn_enabled == id) {
            setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
        } else if (R.id.btn_getVer == id) {
            String version = getApplicationVersionName("");
            Log.d(TAG, "onClick: version=" + version);
        } else if (R.id.btn_enhancedService == id) {
            testGetEnhancedServiceInfoList(this);
        } else if (R.id.btn_test == id) {
        }
    }




    private void setApplicationEnabledSetting(String packageName, int enabledSetting, int flag) {
        mPackageManager.setApplicationEnabledSetting(packageName, enabledSetting, flag);
    }

    /**
     * 判断传入包名的包是否是系统应用
     * @param packageName
     * @return
     */
    private boolean isSystemApp(String packageName) {
        try {
            ApplicationInfo applicationInfo = mPackageManager.getApplicationInfo(packageName, 0);
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                // 是系统应用
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            // 应用不存在
            Log.d(TAG, "isSystemApp: " + packageName + " dose not exist.");
        }
        return false;
    }

    private void showAppPath(String packageName) {
        try {
            ApplicationInfo appInfo = mPackageManager.getApplicationInfo(packageName, 0);
            String sourceDir = appInfo.sourceDir;
            String publicSourceDir = appInfo.publicSourceDir;
//            String classLoaderName = appInfo.classLoaderName;
            Log.d(TAG, "getAppPath: package " + packageName + ", sourceDir=" + sourceDir + "\n"
                    + "publicSourceDir=" + publicSourceDir + "\n"
                    /*+ "classLoaderName=" + classLoaderName*/);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setRpkComponentInfo(Context context) {
        try {
            Intent intent = new Intent();

            Class<?> pmeClass = Class.forName("flyme.pm.PackageManagerExt");
            Method getInstanceMethod = pmeClass.getMethod("getInstance", Context.class);
            Object pmsInstance = getInstanceMethod.invoke(null, context);
            // 获取调用setRpkState方法，打开Rpk功能
            Method setRpkStateMethod = pmeClass.getMethod("setRpkState", boolean.class);
            Object result = setRpkStateMethod.invoke(pmsInstance, true);
            if ((boolean) result) {
                Log.d(TAG, "setRpkComponentInfo: invoke setRpkState succuss.");
            }
            // 获取调用setRpkComponentInfo方法，修改源组件
            Method setRpkComponentInfoMethod = pmeClass.getMethod("setRpkComponentInfo", ComponentName.class, ComponentName.class);
            ComponentName gmsSpaceActivity = ComponentName.unflattenFromString("com.google.android.gms/co.g.Space");
            ComponentName desActivity = new ComponentName(this, EmptyTestActivity.class);
            setRpkComponentInfoMethod.invoke(pmsInstance, gmsSpaceActivity, desActivity);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void isMzApp(Context context, String packageName) {
        try {
            Class<?> pmeClass = Class.forName("flyme.pm.PackageManagerExt");
            Method getInstanceMethod = pmeClass.getMethod("getInstance", Context.class);
            Object pmsInstance = getInstanceMethod.invoke(null, context);

            Method isRestorableMzAppMethod = pmeClass.getMethod("isRestorableMzApp", String.class);
            Object result = isRestorableMzAppMethod.invoke(pmsInstance, packageName);
            Log.d(TAG, "isMzApp: package:" + packageName + ", result=" + result);
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

    /**
     * 获取增强服务模块信息
     * @param context
     */
    private void testGetEnhancedServiceInfoList(Context context) {
        Class<?> pmeClass;
        try {
            pmeClass = Class.forName("flyme.pm.PackageManagerExt");
            Method getInstanceMethod = pmeClass.getMethod("getInstance", Context.class);
            // 获取 flyme.pm.PackageManagerExt 实例
            Object pmsInstance = getInstanceMethod.invoke(null, context);

//            Method getEnhancedServiceInfoListMethod = pmeClass.getMethod("getEnhancedServiceInfoList");
//            List<Object> infoList = (List<Object>) getEnhancedServiceInfoListMethod.invoke(pmsInstance);
//            Class<?> infoClass = Class.forName("flyme.pm.FlymeEnhancedServiceInfo");
//            for (Object info : infoList) {
//                // 无法直接获取字段，通过 get 函数获取
//                Method getPackageNameMethod = infoClass.getMethod("getPackageName");
//                String packageName = (String) getPackageNameMethod.invoke(info);
//                Method getSummaryMethod = infoClass.getMethod("getSummary");
//                String summary = (String) getSummaryMethod.invoke(info);
//                Method getDetailMethod = infoClass.getMethod("getDetail");
//                String detail = (String) getDetailMethod.invoke(info);
//                Log.d(TAG, "testGetEnhancedServiceInfoList: packageName=" + packageName + ", summary=" + summary + ", detail=" + detail + "\n");
//            }

            Method getSwitchablePackagesForCTAMethod = pmeClass.getMethod("getSwitchablePackagesForCTA");
            List<Object> pkgs = (List<Object>) getSwitchablePackagesForCTAMethod.invoke(pmsInstance);
            for (Object pkg : pkgs) {
                Log.d(TAG, "testGetEnhancedServiceInfoList: pkg=" + pkg);
            }

        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                 | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取应用版本号
     * @param packageName
     * @return
     */
    private String getApplicationVersionName(String packageName) {
        String versionName = "";
        try {
            PackageInfo packageInfo = mPackageManager.getPackageInfo(packageName, 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        return versionName;
    }

    /***
     * 获取目标用户的 Context
     * @param context
     * @param userId
     * @param flag
     * @return
     */
    private Context createContextAsUser(Context context, int userId, int flag) {
        Context targetContext;

        try {
            Method createContextAsUserMethod = Context.class.getMethod("createContextAsUser", UserHandle.class, int.class);
            createContextAsUserMethod.setAccessible(true);
            Method ofMethod = UserHandle.class.getMethod("of", int.class);
            ofMethod.setAccessible(true);
            UserHandle userhandle = (UserHandle) ofMethod.invoke(null, userId);
            targetContext = (Context) createContextAsUserMethod.invoke(context, userhandle, flag);
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return targetContext;
    }

    /**
     *
     */
    private void getApplicationEnabledSetting() {
//        if (分身用户) {
            Context targetContext = createContextAsUser(this, 0/*目标用户*/, 0);
            int state = targetContext.getPackageManager().getApplicationEnabledSetting("com.meizu.alphame");
            Log.d(TAG, "testCreateContextAsUser: state=" + state);
//        }
    }

    public boolean getApplicationEnabled(Context context, String enhancedPackageName) {
        boolean enabled;
        int state = context.getPackageManager().getApplicationEnabledSetting(enhancedPackageName);
        if (PackageManager.COMPONENT_ENABLED_STATE_DEFAULT  == state || PackageManager.COMPONENT_ENABLED_STATE_ENABLED == state) {
            enabled = true;
        } else {
            enabled = false;
        }
        return enabled;
    }

//    public boolean getApplicationEnabled(Context context, String enhancedPackageName) {
//        boolean enabled;
//        try {
//            enabled = context.getPackageManager().getApplicationInfo(enhancedPackageName, 0).enabled;
//        } catch (PackageManager.NameNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return enabled;
//    }

    public void getBuildExtField() {
        try {
            Class buildExtClass = Class.forName("android.os.BuildExt");
            Log.d(TAG, "getBuildExtField: buildExtClass=" + buildExtClass);
            Field fieldIsShopDemo = buildExtClass.getDeclaredField("IS_SHOPDEMO");
            fieldIsShopDemo.setAccessible(true);
            boolean IS_SHOPDEMO = (boolean) fieldIsShopDemo.get(null);
            Log.d(TAG, "getBuildExtField: IS_SHOPDEMO=" + IS_SHOPDEMO);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private ApplicationInfo getSourceInfo(String packageName) {

        if (packageName != null) {
            try {
                ApplicationInfo appinfo = getPackageManager().getApplicationInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    private void buildInstallIntent() {
        Intent install = new Intent();
        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        new Thread(() -> {
            File apkDir = getExternalFilesDir(null);
            File apkFile = new File(apkDir,"com.yingyonghui.apk");
            Log.d(TAG, "buildInstallIntent: apkFile=" + apkFile.getAbsolutePath());
            Uri data = Uri.fromFile(apkFile);
            Log.d(TAG, "buildInstallIntent: data=" + data.toString());
            install.setData(data);
            install.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, getPackageName());
        }).start();
    }

    private void getInstalledPackageInfo() {

    }
}