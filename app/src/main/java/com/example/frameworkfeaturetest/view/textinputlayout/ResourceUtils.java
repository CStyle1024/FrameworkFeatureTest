package com.example.frameworkfeaturetest.view.textinputlayout;

import android.content.Context;
import android.content.res.TypedArray;

public class ResourceUtils {
    public static int getMzThemeColor(Context context) {
        int id = context.getResources().getIdentifier("mzThemeColor", "attr", context.getPackageName());
        if (id <= 0) {
            return 0;
        }
        TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{id});
        int color = array.getColor(0, -1);
        array.recycle();
        if (color == -1) {
            return 0;
        }
        return color;
    }
}
