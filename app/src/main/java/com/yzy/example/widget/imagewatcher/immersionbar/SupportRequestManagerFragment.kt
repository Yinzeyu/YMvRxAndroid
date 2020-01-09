package com.yzy.example.widget.imagewatcher.immersionbar

import android.app.Activity
import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @author geyifeng
 * @date 2019/4/11 6:43 PM
 */
class SupportRequestManagerFragment : Fragment() {
    private var mDelegate: ImmersionDelegate? = null
    operator fun get(o: Any?): ImmersionBar? {
        if (mDelegate == null) {
            mDelegate = ImmersionDelegate(o)
        }
        return mDelegate!!.get()
    }

    operator fun get(activity: Activity, dialog: Dialog?): ImmersionBar? {
        if (mDelegate == null) {
            mDelegate = ImmersionDelegate(activity, dialog)
        }
        return mDelegate!!.get()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mDelegate != null) {
            mDelegate!!.onActivityCreated(resources.configuration)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mDelegate != null) {
            mDelegate!!.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDelegate != null) {
            mDelegate!!.onDestroy()
            mDelegate = null
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (mDelegate != null) {
            mDelegate!!.onConfigurationChanged(newConfig)
        }
    }
}