package com.example.frameworkfeaturetest.view.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MzEditInputView extends FrameLayout {
    private final ContentObserver darkModeChangedObserver;

    public MzEditInputView(@NonNull Context context) {
        this(context, null);
    }

    public MzEditInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MzEditInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        darkModeChangedObserver = new ContentObserver(getHandler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
            }
        };
    }
}
