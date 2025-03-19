package com.example.frameworkfeaturetest.view.textinputlayout;

import android.view.View;
import android.view.ViewOutlineProvider;

public class ViewUtilsLollipop {
    ViewUtilsLollipop() {

    }

    public static void setBoundsViewOutlineProvider(View view) {
        view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
    }
}
