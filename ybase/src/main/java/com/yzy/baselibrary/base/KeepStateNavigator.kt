package com.yzy.baselibrary.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import java.util.*

@Navigator.Name("tab_fragment")
class KeepStateNavigator(
    private val mContext: Context,
    private val mFragmentManager: FragmentManager,
    private val mContainerId: Int
) : FragmentNavigator(mContext, mFragmentManager, mContainerId) {
    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination?{

            try {
                // 反射获取mBackStack mIsPendingBackStackOperation
                val mBackStackField = FragmentNavigator::class.java.getDeclaredField("mBackStack")
                mBackStackField.isAccessible = true

                @Suppress("UNCHECKED_CAST")
                val mBackStack: ArrayDeque<Int> = mBackStackField.get(this) as ArrayDeque<Int>

//                val mIsPendingBackStackOperationField =
//                    FragmentNavigator::class.java.getDeclaredField("mIsPendingBackStackOperation")
//                mIsPendingBackStackOperationField.isAccessible = true
                if (mFragmentManager.isStateSaved) {
                    Log.i(
                        "TAG",
                        "Ignoring navigate() call: FragmentManager has already" + " saved its state"
                    )
                    return null
                }
                var className = destination.className
                if (className[0] == '.') {
                    className = mContext.packageName + className
                }
                val ft = mFragmentManager.beginTransaction()
                var enterAnim = navOptions?.enterAnim ?: -1
                var exitAnim = navOptions?.exitAnim ?: -1
                var popEnterAnim = navOptions?.popEnterAnim ?: -1
                var popExitAnim = navOptions?.popExitAnim ?: -1
                if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
                    enterAnim = if (enterAnim != -1) enterAnim else 0
                    exitAnim = if (exitAnim != -1) exitAnim else 0
                    popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
                    popExitAnim = if (popExitAnim != -1) popExitAnim else 0
                    ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
                }
                val tag = destination.id.toString()
                val currentFragment = mFragmentManager.primaryNavigationFragment
                if (currentFragment != null) {
                    ft.hide(currentFragment)
                }
                var frag = mFragmentManager.findFragmentByTag(tag)
                if (frag == null) {
                    frag = mFragmentManager.fragmentFactory.instantiate(
                        mContext.classLoader,
                        className
                    )
                    frag.arguments = args
                    ft.add(mContainerId, frag, tag)
                } else {
                    ft.show(frag)
                }
                ft.setPrimaryNavigationFragment(frag)

                @IdRes val destId = destination.id
                val initialNavigation = mBackStack.isEmpty()

                val isSingleTopReplacement = (navOptions != null && !initialNavigation
                        && navOptions.shouldLaunchSingleTop()
                        && mBackStack.peekLast()?.toInt() == destId)

                val isAdded = when {
                    initialNavigation -> true
                    isSingleTopReplacement -> {
                        // Single Top means we only want one instance on the back stack
                        if (mBackStack.size > 1) {
                            // If the Fragment to be replaced is on the FragmentManager's
                            // back stack, a simple replace() isn't enough so we
                            // remove it from the back stack and put our replacement
                            // on the back stack in its place
                            mFragmentManager.popBackStack(generateMyBackStackName(mBackStack.size, mBackStack.peekLast() ?: 0), FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            ft.addToBackStack(generateMyBackStackName(mBackStack.size, destId))
                        }
                        false
                    }
                    else -> {
                        ft.addToBackStack(generateMyBackStackName(mBackStack.size + 1, destId))
                        true
                    }
                }
                if (navigatorExtras is Extras) {
                    val extras = navigatorExtras as Extras?
                    for ((key, value) in extras!!.sharedElements) {
                        ft.addSharedElement(key, value)
                    }
                }
                ft.setReorderingAllowed(true)
                ft.commit()
                return if (isAdded) {
                    mBackStack.add(destId)
                    destination
                } else {
                    null
                }
                // The commit succeeded, update our view of the world

            } catch (e: Throwable) {
                return super.navigate(destination, args, navOptions, navigatorExtras)
            }
        }

        private fun generateMyBackStackName(backStackIndex: Int, destId: Int): String {
            return "$backStackIndex-$destId"
        }

}

//    override fun popBackStack(): Boolean {
//        if (mBackStack.isEmpty()) {
//            return false
//        }
//        //        if (manager.getBackStackEntryCount() > 0) {
////            manager.popBackStack(
////                    generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
////                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
////        } // else, we're on the first Fragment, so there's nothing to pop from FragmentManager
//        val removeTag = mBackStack.removeLast()
//        return doNavigate(removeTag)
//    }
//
//    /**
//     * 使用场景：A 打开 B, B 打开 C。然后需要关闭 B
//     *
//     * @param destId 是在 Navigation 节点中 keep_state_fragment 中的 id 值，不能是 action 的 id！！！
//     * @return true 移除成功，false 移除失败
//     */
//    fun closeMiddle(destId: Int): Boolean {
//        val removeTag = destId.toString()
//        if (!mBackStack.contains(removeTag)) {
//            return false
//        }
//        val remove = mBackStack.remove(removeTag)
//        return if (remove) {
//            doNavigate(removeTag)
//        } else {
//            false
//        }
//    }
//
//    /**
//     * 使用场景：A 打开 B，B 打开 C。在返回过程中，需要跳过 B 直接回到 A。
//     *
//     * @param destId 是在 Navigation 节点中 keep_state_fragment 中的 id 值，不能是 action 的 id！！！
//     * @return true 移除成功，false 移除失败
//     */
//    fun navigateUp(destId: Int): Boolean {
//        val distTag = destId.toString()
//        if (!mBackStack.contains(distTag)) {
//            return false
//        }
//        val transaction = manager.beginTransaction()
//        while (!mBackStack.isEmpty()) {
//            val tmp = mBackStack.last
//            val fragmentByTag = manager.findFragmentByTag(tmp)
//            if (tmp == distTag) {
//                if (fragmentByTag != null) {
//                    transaction.show(fragmentByTag)
//                    transaction.setPrimaryNavigationFragment(fragmentByTag)
//                    transaction.setReorderingAllowed(true)
//                }
//                break
//            }
//            if (fragmentByTag != null) {
//                mBackStack.removeLast()
//                transaction.remove(fragmentByTag)
//            }
//        }
//        if (manager.isStateSaved) {
//            transaction.commitNowAllowingStateLoss()
//        } else {
//            transaction.commit()
//        }
//        return true
//    }
//
//    /**
//     * 移除 Fragment 并把当前栈顶的 Fragment 显示出来。
//     *
//     * @param removeTag 待移除 Fragment tag
//     * @return true 移除成功，false 移除失败
//     */
//    private fun doNavigate(removeTag: String): Boolean {
//        val transaction = manager.beginTransaction()
//        val removeFrag = manager.findFragmentByTag(removeTag)
//        if (removeFrag != null) {
//            transaction.remove(removeFrag)
//        } else {
//            return false
//        }
//        try {
//            val showTag = mBackStack.last
//            val showFrag = manager.findFragmentByTag(showTag)
//            if (showFrag != null) {
//                transaction.show(showFrag)
//                transaction.setPrimaryNavigationFragment(showFrag)
//                transaction.setReorderingAllowed(true)
//                val stateSaved = manager.isStateSaved
//                Log.d(
//                    TAG,
//                    "popBackStack: 当前是否在进行状态保存$stateSaved"
//                )
//                if (stateSaved) {
//                    transaction.commitNowAllowingStateLoss()
//                } else {
//                    transaction.commitNow()
//                }
//            } else {
//                return false
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return false
//        }
//        return true
//    }
//
//    companion object {
//        const val NODE_NAME = "tab_fragment"
//        private const val TAG = "KeepStateNavigator"
//    }

//}