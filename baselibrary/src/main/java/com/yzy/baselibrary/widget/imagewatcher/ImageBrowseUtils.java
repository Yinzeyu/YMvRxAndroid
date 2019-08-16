package com.yzy.baselibrary.widget.imagewatcher;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/8 16:17
 */
public class ImageBrowseUtils {
  public static ImageBrowse newInstance(FragmentActivity activity, EnterExitListener listener,
      boolean isShowIndex) {
    return ImageBrowse.Companion.newInstance(activity, listener, isShowIndex);
  }

  public static ImageBrowse newInstance(FragmentActivity activity, boolean isShowIndex) {
    return ImageBrowse.Companion.newInstance(activity, null, isShowIndex);
  }

  /** 是否需要大图处理返回，不需要则返回false */
  public static boolean handleBackPressed(FragmentActivity activity) {
    if (activity != null) {
      ViewGroup view = (ViewGroup) activity.getWindow().getDecorView();
      int count = view.getChildCount();
      for (int i = count - 1; i >= 0; i--) {
        View child = view.getChildAt(i);
        if (child instanceof ImageWatcher) {
          return ((ImageWatcher) child).handleBackPressed();
        }
      }
    }
    return false;
  }
}
