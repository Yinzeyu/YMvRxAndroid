package com.yzy.baselibrary.widget.imagewatcher;

import android.net.Uri;
import android.view.View;

/**
 * description :
 *
 * @author : case
 * @date : 2018/8/8 12:24
 */
public class EnterExitListener implements ImageWatcher.OnStateChangedListener {
  @Override
  public final void onStateChangeUpdate(ImageWatcher imageWatcher, View clicked, int position,
                                        Uri uri, float animatedValue, int actionTag) {

  }

  @Override
  public final void onStateChanged(ImageWatcher imageWatcher, int position, Uri uri,
      int actionTag) {
    if (actionTag == ImageWatcher.STATE_ENTER_DISPLAYING) {
      enterImageBrowse();
    } else if (actionTag == ImageWatcher.STATE_EXIT_HIDING) {
      exitImageBrowse();
    }
  }

  /** 进入浏览大图 */
  public void enterImageBrowse() {
  }

  /** 退出大图浏览 */
  public void exitImageBrowse() {
  }
}
