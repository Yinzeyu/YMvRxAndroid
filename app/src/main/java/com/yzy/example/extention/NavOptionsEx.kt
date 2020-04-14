package com.yzy.example.extention

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.Nullable
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.example.R
import com.yzy.example.component.main.MainActivity

//当前属于弹出栈
//app:popUpToInclusive="true/false"
//弹出栈是否包含目标
//app:launchSingleTop="true/false"
//是否开启singleTop模式
//
//app:enterAnim=""
//app:exitAnim=""
//导航动画
//
//app:popEnterAnim=""
//app:popExitAnim=""

val options = navOptions {
    anim {
        enter = R.anim.slide_right_in // 进入页面动画
        exit = R.anim.slide_left_out
        popEnter = R.anim.slide_left_in  // 弹出栈动画
        popExit = R.anim.slide_right_out
    }

//    launchSingleTop = true
//    popUpTo = R.id.categoryFragment
}

fun startNavigate(activity: MainActivity, @IdRes resId: Int, args: Bundle? = null) {
    val rootView = activity.getRootView()
     Navigation.findNavController(rootView).navigate(resId, args)
}

fun startNavigate(activity: MainActivity,  navDirections: NavDirections) {
    val rootView = activity.getRootView()
        Navigation.findNavController(rootView).navigate(navDirections)
}
