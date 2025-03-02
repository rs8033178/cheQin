package com.cheqin.booking.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cheqin.booking.utils.typefaces.TypefaceManager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontTextview extends TextView {
    public CustomFontTextview(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomFontTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypefaceManager.initTypeface(this, context, attrs, defStyle);
    }
}
