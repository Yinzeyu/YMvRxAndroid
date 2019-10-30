package com.yzy.pj.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yzy.commonlibrary.widget.span.LinkTouchMovementMethod;
import com.yzy.pj.R;

import java.lang.ref.WeakReference;

/**
 * description: 可以自动滚动字幕的控件.
 *
 * @date 2018/6/27 12:01.
 * @author: yzy.
 */
public class RollingTextView extends ViewFlipper {
  private static final int MSG_CODE_ROLLING = 101;

  private TextView mTextViewShow;
  private TextView mTextViewHide;
  private DynamicLayout mDynamicLayout;
  private SpannableStringBuilder mSpannableStringBuilder;

  private Animation mAnimationBottomIn;
  private Animation mAnimationTopOut;

  private int currentLine = 0;
  private int maxLine = 0;

  private long counterInterval = 4000L;

  private boolean canRolling = false;
  private boolean rolling = false;

  private RollingTextViewHandler mRollingTextViewHandler;

  public RollingTextView(Context context) {
    super(context);
  }

  public RollingTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView(context);
    initAnimation();
    setView();
  }

  private void initView(Context context) {
    mRollingTextViewHandler = new RollingTextViewHandler(this);
    mTextViewShow =
        (TextView) LayoutInflater.from(context)
            .inflate(R.layout.play_view_text_rollingtext, this, false);
    mTextViewHide =
        (TextView) LayoutInflater.from(context)
            .inflate(R.layout.play_view_text_rollingtext, this, false);
    mTextViewShow.setMovementMethod(new LinkTouchMovementMethod(null));
    mTextViewShow.setHighlightColor(Color.TRANSPARENT);
    mTextViewHide.setMovementMethod(new LinkTouchMovementMethod(null));
    mTextViewHide.setHighlightColor(Color.TRANSPARENT);
  }

  private void initAnimation() {
    mAnimationBottomIn = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f
    );
    mAnimationBottomIn.setDuration(500);
    mAnimationTopOut = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f
    );
    mAnimationTopOut.setDuration(500);
    mAnimationTopOut.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        currentLine++;
        if (currentLine >= maxLine) {
          currentLine = 0;
        }
      }

      @Override
      public void onAnimationEnd(Animation animation) {
        TextView temp = mTextViewShow;
        mTextViewShow = mTextViewHide;
        mTextViewHide = temp;
        int nextLine = currentLine + 1;
        if (nextLine >= maxLine) {
          nextLine = 0;
        }
        setText(nextLine, mTextViewHide);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
      }
    });
  }

  private void setView() {
    setOutAnimation(mAnimationTopOut);
    setInAnimation(mAnimationBottomIn);
    getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      @Override
      public boolean onPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        initLineInfo();
        return true;
      }
    });
  }

  public void reset() {
    canRolling = false;
    removeAllViews();
    mTextViewShow.setText("");
    mTextViewHide.setText("");
    addView(mTextViewShow);
    addView(mTextViewHide);
    currentLine = 0;
    mDynamicLayout = null;
  }

  public void setSpannableStringBuilder(SpannableStringBuilder spannableStringBuilder) {
    this.mSpannableStringBuilder = spannableStringBuilder;
    reset();
    initLineInfo();
  }

  private void initLineInfo() {
    if (getWidth() == 0 || mSpannableStringBuilder == null) {
      return;
    }
    currentLine = 0;
    mDynamicLayout =
        new DynamicLayout(mSpannableStringBuilder, mTextViewShow.getPaint(), getWidth(),
            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    maxLine = mDynamicLayout.getLineCount();
    setText(currentLine, mTextViewShow);
    if ((currentLine + 1) < maxLine) {
      canRolling = true;
      setText((currentLine + 1), mTextViewHide);
    }
    start();
  }

  private void setText(int lineIndex, TextView textView) {
    if (mDynamicLayout == null) {
      return;
    }
    int start = mDynamicLayout.getLineStart(lineIndex);
    int end = mDynamicLayout.getLineEnd(lineIndex);
    textView.setText(mSpannableStringBuilder.subSequence(start, end));
  }

  private void removeMessage() {
    mRollingTextViewHandler.removeMessages(MSG_CODE_ROLLING);
  }

  private void loopNext() {
    removeMessage();
    mRollingTextViewHandler.sendEmptyMessageDelayed(MSG_CODE_ROLLING, counterInterval);
  }

  /**
   * 开始自动滚动
   */
  public void start() {
    if (!canRolling || rolling) {
      return;
    }
    rolling = true;
    removeMessage();
    mRollingTextViewHandler.sendEmptyMessageDelayed(MSG_CODE_ROLLING, counterInterval);
  }

  /**
   * 停止自动滚动
   */
  public void stop() {
    rolling = false;
    removeMessage();
    reset();
  }

  public boolean isRolling() {
    return rolling;
  }

  private static final class RollingTextViewHandler extends Handler {
    WeakReference<RollingTextView> mRollingTextViewWeakReference;

    public RollingTextViewHandler(RollingTextView rollingTextView) {
      mRollingTextViewWeakReference = new WeakReference<>(rollingTextView);
    }

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case MSG_CODE_ROLLING:
          if (mRollingTextViewWeakReference.get() != null) {
            if (!mRollingTextViewWeakReference.get().isRolling()) {
              return;
            }
            mRollingTextViewWeakReference.get().showNext();
            mRollingTextViewWeakReference.get().loopNext();
          }

          break;
        default:
          break;
      }
    }
  }
}
