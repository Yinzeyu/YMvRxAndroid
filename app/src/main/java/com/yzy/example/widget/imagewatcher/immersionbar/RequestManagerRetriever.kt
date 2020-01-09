package com.yzy.example.widget.imagewatcher.immersionbar

import android.app.Activity
import android.app.Dialog
import android.app.FragmentManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.yzy.example.widget.imagewatcher.immersionbar.ImmersionBar
import java.util.*

/**
 * @author geyifeng
 * @date 2019/4/12 4:21 PM
 */
internal class RequestManagerRetriever private constructor() : Handler.Callback {
    private val mTag = ImmersionBar::class.java.name
    private val mHandler: Handler

    private object Holder {
        val INSTANCE = RequestManagerRetriever()
    }

    private val mPendingFragments: MutableMap<FragmentManager, RequestManagerFragment> =
        HashMap()
    private val mPendingSupportFragments: MutableMap<androidx.fragment.app.FragmentManager, SupportRequestManagerFragment> =
        HashMap()

    operator fun get(activity: Activity): ImmersionBar? {
        checkNotNull(
            activity,
            "activity is null"
        )
        return if (activity is FragmentActivity) {
            getSupportFragment(
                activity.supportFragmentManager,
                mTag + activity.toString()
            )!![activity]
        } else {
            getFragment(activity.fragmentManager, mTag + activity.toString())!![activity]
        }
    }

    operator fun get(fragment: Fragment): ImmersionBar? {
        checkNotNull(
            fragment,
            "fragment is null"
        )
        checkNotNull<FragmentActivity?>(
            fragment.activity,
            "fragment.getActivity() is null"
        )
        if (fragment is DialogFragment) {
            checkNotNull<Dialog?>(
                fragment.dialog,
                "fragment.getDialog() is null"
            )
        }
        return getSupportFragment(
            fragment.childFragmentManager,
            mTag + fragment.toString()
        )!![fragment]
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    operator fun get(fragment: android.app.Fragment): ImmersionBar? {
        checkNotNull(
            fragment,
            "fragment is null"
        )
        checkNotNull(
            fragment.activity,
            "fragment.getActivity() is null"
        )
        if (fragment is android.app.DialogFragment) {
            checkNotNull(
                fragment.dialog,
                "fragment.getDialog() is null"
            )
        }
        return getFragment(fragment.childFragmentManager, mTag + fragment.toString())!![fragment]
    }

    operator fun get(activity: Activity, dialog: Dialog): ImmersionBar? {
        checkNotNull(
            activity,
            "activity is null"
        )
        checkNotNull(
            dialog,
            "dialog is null"
        )
        return if (activity is FragmentActivity) {
            getSupportFragment(
                activity.supportFragmentManager,
                mTag + dialog.toString()
            )!![activity, dialog]
        } else {
            getFragment(
                activity.fragmentManager,
                mTag + dialog.toString()
            )!![activity, dialog]
        }
    }

    fun destroy(activity: Activity?, dialog: Dialog?) {
        if (activity == null || dialog == null) {
            return
        }
        if (activity is FragmentActivity) {
            val fragment =
                getSupportFragment(
                    activity.supportFragmentManager,
                    mTag + dialog.toString(),
                    true
                )
            if (fragment != null) {
                fragment[activity, dialog]!!.destroy()
            }
        } else {
            val fragment =
                getFragment(activity.fragmentManager, mTag + dialog.toString(), true)
            if (fragment != null) {
                fragment[activity, dialog]!!.destroy()
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        var handled = true
        when (msg.what) {
            ID_REMOVE_FRAGMENT_MANAGER -> {
                val fm = msg.obj as FragmentManager
                mPendingFragments.remove(fm)
            }
            ID_REMOVE_SUPPORT_FRAGMENT_MANAGER -> {
                val supportFm =
                    msg.obj as androidx.fragment.app.FragmentManager
                mPendingSupportFragments.remove(supportFm)
            }
            else -> handled = false
        }
        return handled
    }

    private fun getFragment(
        fm: FragmentManager,
        tag: String
    ): RequestManagerFragment? {
        return getFragment(fm, tag, false)
    }

    private fun getFragment(
        fm: FragmentManager,
        tag: String,
        destroy: Boolean
    ): RequestManagerFragment? {
        var fragment: RequestManagerFragment? =
            fm.findFragmentByTag(tag) as RequestManagerFragment
        if (fragment == null) {
            fragment = mPendingFragments[fm]
            if (fragment == null) {
                if (destroy) {
                    return null
                }
                fragment = RequestManagerFragment()
                mPendingFragments[fm] = fragment
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss()
                mHandler.obtainMessage(
                    ID_REMOVE_FRAGMENT_MANAGER,
                    fm
                ).sendToTarget()
            }
        }
        if (destroy) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
            return null
        }
        return fragment
    }

    private fun getSupportFragment(
        fm: androidx.fragment.app.FragmentManager,
        tag: String
    ): SupportRequestManagerFragment? {
        return getSupportFragment(fm, tag, false)
    }

    private fun getSupportFragment(
        fm: androidx.fragment.app.FragmentManager,
        tag: String,
        destroy: Boolean
    ): SupportRequestManagerFragment? {
        var fragment =
            fm.findFragmentByTag(tag) as SupportRequestManagerFragment?
        if (fragment == null) {
            fragment = mPendingSupportFragments[fm]
            if (fragment == null) {
                if (destroy) {
                    return null
                }
                fragment =
                    SupportRequestManagerFragment()
                mPendingSupportFragments[fm] = fragment
                fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss()
                mHandler.obtainMessage(
                    ID_REMOVE_SUPPORT_FRAGMENT_MANAGER,
                    fm
                ).sendToTarget()
            }
        }
        if (destroy) {
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
            return null
        }
        return fragment
    }

    companion object {
        private const val ID_REMOVE_FRAGMENT_MANAGER = 1
        private const val ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2
        val instance: RequestManagerRetriever
            get() = Holder.INSTANCE

        private fun <T> checkNotNull(arg: T?, message: String) {
            if (arg == null) {
                throw NullPointerException(message)
            }
        }
    }

    init {
        mHandler = Handler(Looper.getMainLooper(), this)
    }
}