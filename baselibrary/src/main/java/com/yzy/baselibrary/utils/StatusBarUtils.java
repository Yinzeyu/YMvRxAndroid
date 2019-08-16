package com.yzy.baselibrary.utils;

import android.content.Context;

import java.util.Objects;

/**
 * description: 状态栏工具类
 *
 * @date 2018/6/19 20:09.
 * @author: YangYang.
 */
public class StatusBarUtils {
  /**
   * 获取状态栏高度
   */
  public static int getStatusBarHeight(Context context) {
    int height = getStatusBarHeightMethod1(context);
    if (height <= 0) {
      height = getStatusBarHeightMethod2(context);
    }
    return height;
  }

  private static int getStatusBarHeightMethod1(Context context) {
    int statusBarHeight = -1;
    //获取status_bar_height资源的ID
    int resourceId = context.getResources()
        .getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      //根据资源ID获取响应的尺寸值
      statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
    }
    return statusBarHeight;
  }

  private static int getStatusBarHeightMethod2(Context context) {
    int statusBarHeight = -1;
    try {
      Class<?> clazz = Class.forName("com.android.internal.R$dimen");
      Object object = clazz.newInstance();
      int height = Integer.parseInt(Objects.requireNonNull(clazz.getField("status_bar_height")
              .get(object)).toString());
      statusBarHeight = context.getResources().getDimensionPixelSize(height);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return statusBarHeight;
  }
}
