package com.yzy.example.utils.permisstions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.yzy.example.utils.permisstions.callbacks.FailCallback
import com.yzy.example.utils.permisstions.callbacks.RequestResultListener
import com.yzy.example.utils.permisstions.callbacks.SuccessCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.Reference
import java.lang.ref.WeakReference

class InlinePermissionResult {
    private val TAG = "ACTIVITY_RESULT_FRAGMENT_WEEEEE"
    private var activityReference: Reference<FragmentActivity>
    private val successCallbacks=ArrayList<SuccessCallback>()
    private val failCallbacks=ArrayList<FailCallback>()
    private val responseListeners=ArrayList<RequestResultListener>()
    private var listener=object : RequestPermissionFragment.RequestPermissionsListener {
        override fun onRequestPermissions(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            //如果grantResults中，0代表该权限请求成功，-1代表失败
            var isRequestSuccess=true
            for (code in grantResults){
                if (code==-1) {
                    isRequestSuccess=false
                    break
                }
            }
            if (isRequestSuccess){
                for (callback in successCallbacks) {
                    callback.onSuccess()
                }
                for (listener in responseListeners){
                    listener.onSuccess()
                }
            }else{
                for (callback in failCallbacks){
                    callback.onFailed()
                }
                for (listener in responseListeners){
                    listener.onFailed()
                }
            }

        }
    }
    constructor(activity: FragmentActivity){
        activityReference= WeakReference(activity)
    }
    constructor(fragment: Fragment){
        var activity: FragmentActivity?
        fragment.let {
            activity=fragment.activity
        }
        activityReference= WeakReference(activity)
    }

    fun onSuccess(callback: SuccessCallback): InlinePermissionResult {
        callback.let {
            successCallbacks.add(it)
        }
        return this
    }
    fun onFail(callback: FailCallback): InlinePermissionResult {
        callback.let {
            failCallbacks.add(it)
        }
        return this
    }

    fun requestPermissions(vararg permissions:String){
        val activity=activityReference.get()
        if (activity==null || activity.isFinishing)return

        val oldFragment: RequestPermissionFragment?= activity.supportFragmentManager.findFragmentByTag(TAG) as RequestPermissionFragment?
        if (oldFragment!=null){
            oldFragment.setListener(listener)
        }else{
            val newFragment= RequestPermissionFragment.newInstance(*permissions)
            newFragment.setListener(listener)
            CoroutineScope(Dispatchers.Main).launch {
                activity.supportFragmentManager
                    .beginTransaction()
                    .add(newFragment, TAG)
                    .commitNowAllowingStateLoss()
            }
        }

    }
}