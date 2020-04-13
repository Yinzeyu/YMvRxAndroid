package com.yzy.baselibrary.base

import android.os.Bundle
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.blankj.utilcode.util.LogUtils


/***
 *
 */
class NavigateManager {
    companion object {
        val instance: NavigateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { NavigateManager() }
    }

    private var destination: NavDestination? = null
    private var args: Bundle? = null
    private var navOptions: NavOptions? = null
    private var navigatorExtras: Navigator.Extras? = null

    fun setDestination(actions: NavDestination?) {
        LogUtils.e(""+actions)
        destination = actions
    }
    fun setArgs(argsBundle: Bundle?) {
        args = argsBundle
    }
    fun setNavOptions(navOption: NavOptions?) {
        navOptions = navOption
    }
    fun setNavigatorExtras(extras: Navigator.Extras?) {
        navigatorExtras = extras
    }

    fun getDestination( ) :NavDestination?{
        return  destination
    }
    fun getArgs():Bundle? {
        return  args
    }
    fun getNavOptions() :NavOptions?{
      return  navOptions
    }
    fun getNavigatorExtras(): Navigator.Extras?{
      return  navigatorExtras
    }

}