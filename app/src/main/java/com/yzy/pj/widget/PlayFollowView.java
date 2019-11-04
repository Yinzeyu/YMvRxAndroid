package com.yzy.pj.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yzy.pj.R;


/**
 * description: 播放页关注按钮.
 *
 * @date 2018/8/30 10:21.
 * @author: yzy.
 */
public class PlayFollowView extends FrameLayout {

    /**
     * 添加关注按钮
     */
    private ImageView mImageViewAdd;
    /**
     * 关注成功按钮
     */
    private ImageView mImageViewAddOk;
    /**
     * 关注成功的动画
     */
    private AnimatorSet mAnimatorSetAddOk;
    /**
     * 添加关注隐藏动画
     */
    private ObjectAnimator mAnimatorAddHide;

    public PlayFollowView(@NonNull Context context) {
        this(context, null);
    }

    public PlayFollowView(@NonNull Context context,
                          @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayFollowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化View
     */
    private void initView(Context context) {
        mImageViewAddOk = new ImageView(context);
        mImageViewAddOk.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageViewAddOk.setVisibility(GONE);
        mImageViewAddOk.setImageResource(R.drawable.ic_play_followed);
        FrameLayout.LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mImageViewAdd = new ImageView(context);
        mImageViewAdd.setScaleType(ImageView.ScaleType.CENTER);
        mImageViewAdd.setBackgroundResource(R.drawable.play_bg_play_add);
        mImageViewAdd.setImageResource(R.drawable.ic_add);
        addView(mImageViewAddOk, layoutParams);
        addView(mImageViewAdd, layoutParams);
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        if (mAnimatorSetAddOk == null) {
            mAnimatorAddHide = ObjectAnimator.ofFloat(mImageViewAdd, "alpha", 1f, 0f);
            mAnimatorAddHide.setInterpolator(new LinearInterpolator());
            mAnimatorAddHide.setDuration(300);
            mAnimatorAddHide.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mImageViewAdd != null) {
                        mImageViewAdd.setVisibility(View.INVISIBLE);
                        mImageViewAdd.setAlpha(1f);
                    }
                }
            });
            ObjectAnimator animShow = ObjectAnimator.ofFloat(mImageViewAddOk, "alpha", 0, 1f);
            animShow.setInterpolator(new LinearInterpolator());
            animShow.setStartDelay(150);
            animShow.setDuration(300);
            ObjectAnimator animHide = ObjectAnimator.ofFloat(mImageViewAddOk, "alpha", 1f, 0f);
            animHide.setInterpolator(new LinearInterpolator());
            animHide.setStartDelay(1000);
            animHide.setDuration(300);
            mAnimatorSetAddOk = new AnimatorSet();
            mAnimatorSetAddOk.play(animShow).before(animHide);
            mAnimatorSetAddOk.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mImageViewAddOk != null) {
                        mImageViewAddOk.setVisibility(View.GONE);
                        mImageViewAddOk.setAlpha(1f);
                    }
                    PlayFollowView.this.setVisibility(GONE);
                }
            });
        }
    }

    /**
     * 播放关注成功的动画
     */
    public void startOnAnimation() {
        initAnimation();
        mImageViewAdd.setAlpha(1f);
        mImageViewAdd.setVisibility(View.VISIBLE);
        mImageViewAddOk.setAlpha(0f);
        mImageViewAddOk.setVisibility(View.VISIBLE);
        if (mAnimatorAddHide != null) {
            mAnimatorAddHide.start();
        }
        if (mAnimatorSetAddOk != null) {
            mAnimatorSetAddOk.start();
        }
    }

    /**
     * 重置View
     */
    private void resetView() {
        mImageViewAdd.setVisibility(View.VISIBLE);
        mImageViewAdd.setAlpha(1f);
        mImageViewAddOk.setVisibility(View.GONE);
        mImageViewAddOk.setAlpha(1f);
    }

    /**
     * 停止动画
     */
    public void stopAnimation() {
        if (mAnimatorSetAddOk != null) {
            mAnimatorSetAddOk.cancel();
            mImageViewAddOk.clearAnimation();
        }
        if (mAnimatorAddHide != null) {
            mAnimatorAddHide.cancel();
            mImageViewAdd.clearAnimation();
        }
        resetView();
    }
}
