package com.yzy.baselibrary.widget.refresh.wrapper;

import android.annotation.SuppressLint;
import android.view.View;

import com.yzy.baselibrary.widget.refresh.api.RefreshHeader;
import com.yzy.baselibrary.widget.refresh.simple.SimpleComponent;


/**
 * 刷新头部包装
 * Created by scwang on 2017/5/26.
 */
@SuppressLint("ViewConstructor")
public class RefreshHeaderWrapper extends SimpleComponent implements RefreshHeader {

    public RefreshHeaderWrapper(View wrapper) {
        super(wrapper);
    }

}
