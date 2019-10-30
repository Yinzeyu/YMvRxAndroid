package com.yzy.pj.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.yzy.pj.R;

/**
 * description: 播放页进度条.
 *
 * @date 2018/6/21 18:31.
 * @author: yzy.
 */
public class AimyPlayerSeekBar extends View {
  //按下时的X坐标
  float oldX;
  //按下时的Y坐标
  float oldY;
  //按下时的进度
  float oldProgress;
  //click的时间
  long downTime;
  //是否点中圆点
  boolean isTouchPoint = false;
  //上一次触发点击事件的时间
  long lastClickTime = 0;
  //是否执行了开始拖动
  boolean startTrackingTouch;
  private Context context;
  private int width;
  private int height;
  private Paint paint;
  private Paint paintBitmap;
  private Bitmap bitmap;
  private long progress;
  private long duration;
  private int heightProgressLine;
  private int bgProgressLine;
  private OnSeekBarChangeListener onSeekBarChangeListener;
  private boolean isIs16To9;
  private int leftMargin;//计算进度需要用到左边的间距

  public AimyPlayerSeekBar(Context context) {
    super(context);
    init(context);
  }

  public AimyPlayerSeekBar(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public AimyPlayerSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    heightProgressLine = dp2px(context, 2f);
    bgProgressLine = dp2px(context, 1f);
    paint = new Paint();
    paintBitmap = new Paint();
    paint.setStyle(Paint.Style.FILL);
    paint.setColor(Color.WHITE);
    paint.setStrokeCap(Paint.Cap.ROUND);
    bitmap = BitmapFactory.decodeResource(getContext().getResources(),
        R.drawable.ic_progress_shandow);
    this.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout() {
            if (getWidth() > 0) {
              int[] location = new int[2];
              AimyPlayerSeekBar.this.getLocationOnScreen(location);
              leftMargin = location[0];
              AimyPlayerSeekBar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
          }
        });
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    width = w;
    height = h;
  }

  @Override
  public void draw(Canvas canvas) {
    super.draw(canvas);
    if (height <= 0 || bitmap.getWidth() == 0) {
      return;
    }
    //绘制背景
    paint.setStrokeWidth(bgProgressLine);
    paint.setAlpha(76);
    canvas.drawLine(bitmap.getWidth() / 2, height / 2,
        width - bitmap.getWidth() / 2,
        height / 2, paint);
    if (duration <= 0 || progress == 0) {
      //绘制当前进度圆点
      canvas.drawBitmap(bitmap, 0, height / 2 - bitmap.getHeight() / 2, paintBitmap);
    } else {
      //绘制进度
      paint.setStrokeWidth(heightProgressLine);
      paint.setAlpha(255);
      canvas.drawLine(bitmap.getWidth() / 2, height / 2,
          bitmap.getWidth() / 2 + progress * 1f / duration * (width - bitmap.getWidth()),
          height / 2, paint);
      //绘制当前进度圆点
      canvas.drawBitmap(bitmap, progress * 1f / duration * (width - bitmap.getWidth()),
          height / 2 - bitmap.getHeight() / 2, paintBitmap);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (duration <= 0 || !isEnabled()) {
      return true;
    }
    switch (MotionEvent.ACTION_MASK & event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        oldX = event.getRawX();
        oldY = event.getRawY();
        oldProgress = progress;
        downTime = System.currentTimeMillis();
        if (onSeekBarChangeListener != null) {
          onSeekBarChangeListener.onStartTrackingTouch();
          startTrackingTouch = true;
        }
        isTouchPoint = event.getX() > progress * 1f / duration * (width - bitmap.getWidth())
            && event.getX()
            < progress * 1f / duration * (width - bitmap.getWidth()) + bitmap.getWidth();
        return true;
      case MotionEvent.ACTION_MOVE:
        if (isTouchPoint && startTrackingTouch) {
          float change = event.getRawX() - oldX;
          if (isIs16To9) {
            change = event.getRawY() - oldY;
          }
          if (Math.abs(change) > 2) {
            progress = (int) (oldProgress + duration * (change / (width - bitmap.getWidth())));
            if (progress < 0) {
              progress = 0;
            } else if (progress > duration) {
              progress = duration;
            }
            postInvalidate();
            if (onSeekBarChangeListener != null) {
              onSeekBarChangeListener.onMovingProgress(progress);
            }
          }
        }
        return true;
      case MotionEvent.ACTION_UP:
        //单击
        if (!isTouchPoint
            && Math.abs(event.getRawX() - oldX) < 10
            && System.currentTimeMillis() - downTime < 250
            && System.currentTimeMillis() - lastClickTime > 500) {//两次单击时间不能小于0.5秒
          if (event.getX() - leftMargin <= bitmap.getWidth() / 2) {
            progress = 0;
          } else if (event.getX() - leftMargin >= width - bitmap.getWidth() / 2) {
            progress = duration;
          } else {
            progress = (int) ((event.getX() - leftMargin - bitmap.getWidth() / 2)
                / (width - bitmap.getWidth()) * duration);
          }
          postInvalidate();
          lastClickTime = System.currentTimeMillis();
        }
        if (onSeekBarChangeListener != null && startTrackingTouch) {
          onSeekBarChangeListener.onMovingProgress(progress);
          onSeekBarChangeListener.onProgressChanged(progress);
          onSeekBarChangeListener.onStopTrackingTouch();
        }
        startTrackingTouch = false;
        return true;
      default:
        break;
    }
    return super.onTouchEvent(event);
  }

  //尺寸转换
  private int dp2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  public long getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
    if (this.progress > duration) {
      this.progress = duration;
    }
    postInvalidate();
  }

  public long getMax() {
    return duration;
  }

  public void setMax(long duration) {
    this.duration = duration;
  }

  public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
    this.onSeekBarChangeListener = onSeekBarChangeListener;
  }

  public void setIs16To9(boolean mIs16To9) {
    isIs16To9 = mIs16To9;
  }

  public interface OnSeekBarChangeListener {
    void onProgressChanged(long progress);

    void onMovingProgress(long progress);

    void onStartTrackingTouch();

    void onStopTrackingTouch();
  }
}
