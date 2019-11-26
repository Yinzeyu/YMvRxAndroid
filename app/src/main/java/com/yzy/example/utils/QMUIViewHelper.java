package com.yzy.example.utils;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;


public class QMUIViewHelper {

    private static void setBackground(View view, Drawable drawable) {
        view.setBackground(drawable);
    }

    public static void setBackgroundKeepingPadding(View view, Drawable drawable) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        setBackground(view, drawable);
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    public static void setBackgroundKeepingPadding(View view, int backgroundResId) {
        setBackgroundKeepingPadding(view, ContextCompat.getDrawable(view.getContext(), backgroundResId));
    }

    public static void setBackgroundColorKeepPadding(View view, @ColorInt int color) {
        int[] padding = new int[]{view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom()};
        view.setBackgroundColor(color);
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }


}