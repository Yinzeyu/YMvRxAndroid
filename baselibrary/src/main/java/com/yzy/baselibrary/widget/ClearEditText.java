package com.yzy.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.yzy.baselibrary.R;

/**
 * Author liqikun
 * Date on 2015/11/9.
 * 带清除按钮的输入框
 *
 * @author yin97
 */
public class ClearEditText extends AppCompatEditText implements TextWatcher {

    private Drawable mClearDrawable;
    private onTextClearListener mOnTextClearListener;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 获取drawableRight
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            // 如果为空，即没有设置drawableRight，则使用R.mipmap.close这张图片
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText);

            mClearDrawable = typedArray.getDrawable(R.styleable.ClearEditText_rightDrawable);
            typedArray.recycle();
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        addTextChangedListener(this);
        // 默认隐藏图标
        setDrawableVisible(false);
    }

    /**
     * 我们无法直接给EditText设置点击事件，只能通过按下的位置来模拟clear点击事件
     * 当我们按下的位置在图标包括图标到控件右边的间距范围内均算有效
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                // 起始位置
                int start = getWidth() - getTotalPaddingRight() + getPaddingRight();
                // 结束位置
                int end = getWidth();
                boolean available = (event.getX() > start) && (event.getX() < end);
                if (available) {
                    this.setText("");
                    if (mOnTextClearListener != null) {
                        mOnTextClearListener.onClearClick();
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        setDrawableVisible(s.length() > 0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    protected void setDrawableVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right,
                getCompoundDrawables()[3]);
    }

    public void setOnTextClearListener(onTextClearListener onTextClearListener) {
        mOnTextClearListener = onTextClearListener;
    }

    public interface onTextClearListener {
        void onClearClick();
    }
}
