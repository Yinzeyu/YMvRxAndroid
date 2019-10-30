package com.yzy.pj.widget;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

/**
 * description: 全屏loading动画 执行完一遍动画2秒。截止2018年3月3日 15:23:13用到的地方有:耗时的弹窗LoadingDialog、发现(旧版本的乐圈)、Activity基类和Fragment基类
 *
 * @author : yzy
 * @date : 2018/3/3  10:22 创建
 */
public class AimyLoadingView extends View {
  //第一个圆(初始化大的那个)的颜色
  private static int colorCircle1 = Color.parseColor("#3356c66b");//颜色：#56c66b,不透明度：20%
  //第二个圆(初始化小的那个)最小时的颜色
  private static int colorCircle21 = Color.parseColor("#B356c66b");//颜色：#56c66b,不透明度：70%）
  //第二个圆(初始化小的那个)最大时的颜色
  private static int colorCircle22 = Color.parseColor("#6B56c66b");//颜色：#56c66b,不透明度42%
  //宽度
  private int width;
  //高度
  private int height;
  //绘制圆形的
  private Paint paint;
  //颜色变化
  private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
  //最大圆形半径23dp
  private float maxRadius;
  //最小圆形半径4dp
  private float minRadius;
  //圆形1的半径
  private float radiusCircle1;
  //圆形2的半径
  private float radiusCircle2;
  //正在执行的动画
  private ValueAnimator valueAnimator;
  //是否在执行动画
  private boolean isAnimating;

  public AimyLoadingView(Context context) {
    super(context);
    init(context);
  }

  public AimyLoadingView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public AimyLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (w > 0 && h > 0) {
      width = w;
      height = h;
      if (width < maxRadius) {
        maxRadius = width;
        minRadius = maxRadius * 4f / 23;
        radiusCircle1 = maxRadius;
        radiusCircle2 = minRadius;
      }
    }
  }

  //初始化
  private void init(Context context) {
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.FILL);
    maxRadius = dp2px(context, 23 / 2f);
    minRadius = dp2px(context, 4 / 2f);
    radiusCircle1 = maxRadius;
    radiusCircle2 = minRadius;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    paint.setColor(colorCircle1);
    //绘制圆形1
    canvas.drawCircle(width / 2f, height / 2f, radiusCircle1, paint);
    paint.setColor(getCircle2Color(radiusCircle2));
    //绘制圆形2
    canvas.drawCircle(width / 2f, height / 2f, radiusCircle2, paint);
  }

  //根据进度获取半径(整个动画过程从0到1)
  private void refreshRadius(@FloatRange(from = 0, to = 1f) float progress) {
    //经过两点计算直线斜率:y=kx+b
    radiusCircle1 = minRadius + (Math.abs((maxRadius - minRadius) * (1 - 2f * progress)));
    radiusCircle2 = maxRadius - (Math.abs((maxRadius - minRadius) * (2f * progress - 1)));
  }

  //根据圆形的大小获取当前需要显示的颜色值
  private int getCircle2Color(float radius) {
    return (int) argbEvaluator.evaluate((radius - minRadius) * 1f / (maxRadius - minRadius),
        colorCircle21, colorCircle22);
  }

  //dp转px
  private float dp2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return dpValue * scale;
  }

  public void startAnimation() {
    if (isAnimating) {
      return;
    }
    isAnimating = true;
    valueAnimator = ValueAnimator.ofFloat(0, 0.5f, 1f);
    valueAnimator.setDuration(2000);
    valueAnimator.setInterpolator(new LinearInterpolator());
    valueAnimator.addUpdateListener(animation -> {
      refreshRadius((Float) animation.getAnimatedValue());
      postInvalidate();
    });
    valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    valueAnimator.start();
  }

  public void stopAnimation() {
    if (valueAnimator != null && isAnimating) {
      valueAnimator.removeAllUpdateListeners();
      valueAnimator.cancel();
      postInvalidate();
    }
    isAnimating = false;
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    stopAnimation();
  }

  @Override
  public void setVisibility(int visibility) {
    super.setVisibility(visibility);
    if (visibility == VISIBLE) {
      startAnimation();
    } else {
      stopAnimation();
    }
  }
}
