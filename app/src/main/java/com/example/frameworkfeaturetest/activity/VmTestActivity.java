package com.example.frameworkfeaturetest.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.frameworkfeaturetest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dalvik.system.DexClassLoader;

public class VmTestActivity extends AppCompatActivity {

    private static final String TAG = VmTestActivity.class.getSimpleName();

    private static final String sDexFileName = "classes.dex";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vm_test);
    }

    public void onClickVm(View v) {
        Log.d(TAG, "onClick: ");
        int viewId = v.getId();
        if (R.id.btn_custom_cl == viewId) {
            testDexClassLoader();
        }
    }

    /**
     * 需要提前在 files 目录下推好包
     */
    private void testDexClassLoader() {
        File filesDir = getFilesDir();
        File dexFile = new File(filesDir, sDexFileName);
        String dexPath = dexFile.getAbsolutePath();

        copyDexFile(sDexFileName, dexPath);
        File dexOutputDir = getCodeCacheDir();
        String outPutPath = dexOutputDir.getAbsolutePath();
        Log.d(TAG, "testDex: dexPath=" + dexPath + ", outputDir=" + dexOutputDir.getAbsolutePath());

        DexClassLoader myClassLoader = new DexClassLoader(dexPath, outPutPath, null, getClassLoader());
        try {
            Class<?> myGenericClass = myClassLoader.loadClass("com.example.mytest.generic.MyGenericClazz");
            Object instance = myGenericClass.newInstance();
            Log.d(TAG, "testDex: instance=" + instance);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            Log.d(TAG, "testDex: ", e);
            throw new RuntimeException(e);
        }
    }

    private void copyDexFile(String assetPath, String targetPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = getAssets().open(assetPath);
            fos = new FileOutputStream(targetPath);
            byte[] buffer = new byte[4 * 1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null)
                    is.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}