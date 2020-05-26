package com.yzy.baselibrary.extention

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment

/**
 * 作者　: yzy
 * 时间　: 2020/5/26
 * 描述　:
 */
fun Fragment.nav(): NavController {
    return NavHostFragment.findNavController(this)
}

fun nav(view:View): NavController {
    return Navigation.findNavController(view)
}

