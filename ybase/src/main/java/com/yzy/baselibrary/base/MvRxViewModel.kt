package com.yzy.baselibrary.base

import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.MvRxState

/**
 *description: MvRxViewModel.
 *@date 2019/7/15
 *@author: yzy.
 */
abstract class MvRxViewModel<S : MvRxState>(initialState: S) : BaseMvRxViewModel<S>(initialState)