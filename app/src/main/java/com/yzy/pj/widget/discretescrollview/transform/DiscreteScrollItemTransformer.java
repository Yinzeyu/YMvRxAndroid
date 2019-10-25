package com.yzy.pj.widget.discretescrollview.transform;

import android.view.View;

public interface DiscreteScrollItemTransformer {
  void transformItem(View item, float position);
}