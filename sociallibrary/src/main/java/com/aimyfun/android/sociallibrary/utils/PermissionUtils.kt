package com.aimyfun.android.sociallibrary.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.aimyfun.android.sociallibrary.utils.PermissionConstants
import java.util.ArrayList
import java.util.Arrays
import java.util.LinkedHashSet

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/25 15:28
 */
class PermissionUtils private constructor(vararg permissions: String) {

  private var mOnRationaleListener: OnRationaleListener? = null
  private var mSimpleCallback: SimpleCallback? = null
  private var mFullCallback: FullCallback? = null
  private var mThemeCallback: ThemeCallback? = null
  private val mPermissions: MutableSet<String>
  private var mPermissionsRequest: MutableList<String>? = null
  private var mPermissionsGranted: MutableList<String>? = null
  private var mPermissionsDenied: MutableList<String>? = null
  private var mPermissionsDeniedForever: MutableList<String>? = null

  init {
    mPermissions = LinkedHashSet()
    for (permission in permissions) {
      for (aPermission in PermissionConstants.getPermissions(permission)) {
        if (PERMISSIONS.contains(aPermission)) {
          mPermissions.add(aPermission)
        }
      }
    }
    sInstance = this
  }

  /**
   * Set rationale listener.
   *
   * @param listener The rationale listener.
   * @return the single [PermissionUtils] instance
   */
  fun rationale(listener: OnRationaleListener): PermissionUtils {
    mOnRationaleListener = listener
    return this
  }

  /**
   * Set the simple call back.
   *
   * @param callback the simple call back
   * @return the single [PermissionUtils] instance
   */
  fun callback(callback: SimpleCallback): PermissionUtils {
    mSimpleCallback = callback
    return this
  }

  /**
   * Set the full call back.
   *
   * @param callback the full call back
   * @return the single [PermissionUtils] instance
   */
  fun callback(callback: FullCallback): PermissionUtils {
    mFullCallback = callback
    return this
  }

  /**
   * Set the theme callback.
   *
   * @param callback The theme callback.
   * @return the single [PermissionUtils] instance
   */
  fun theme(callback: ThemeCallback): PermissionUtils {
    mThemeCallback = callback
    return this
  }

  /**
   * Start request.
   */
  fun request() {
    mPermissionsGranted = ArrayList()
    mPermissionsRequest = ArrayList()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      mPermissionsGranted!!.addAll(mPermissions)
      requestCallback()
    } else {
      for (permission in mPermissions) {
        if (isGranted(permission)) {
          mPermissionsGranted!!.add(permission)
        } else {
          mPermissionsRequest!!.add(permission)
        }
      }
      if (mPermissionsRequest!!.isEmpty()) {
        requestCallback()
      } else {
        startPermissionActivity()
      }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  private fun startPermissionActivity() {
    mPermissionsDenied = ArrayList()
    mPermissionsDeniedForever = ArrayList()
    PermissionActivity.start(Utils.getApp())
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  private fun rationale(activity: Activity): Boolean {
    var isRationale = false
    if (mOnRationaleListener != null) {
      for (permission in mPermissionsRequest!!) {
        if (activity.shouldShowRequestPermissionRationale(permission)) {
          getPermissionsStatus(activity)
          mOnRationaleListener!!.rationale(object : OnRationaleListener.ShouldRequest {
            override fun again(again: Boolean) {
              if (again) {
                startPermissionActivity()
              } else {
                requestCallback()
              }
            }
          })
          isRationale = true
          break
        }
      }
      mOnRationaleListener = null
    }
    return isRationale
  }

  private fun getPermissionsStatus(activity: Activity) {
    for (permission in mPermissionsRequest!!) {
      if (isGranted(permission)) {
        mPermissionsGranted!!.add(permission)
      } else {
        mPermissionsDenied!!.add(permission)
        if (!activity.shouldShowRequestPermissionRationale(permission)) {
          mPermissionsDeniedForever!!.add(permission)
        }
      }
    }
  }

  private fun requestCallback() {
    if (mSimpleCallback != null) {
      if (mPermissionsRequest!!.size == 0 || mPermissions.size == mPermissionsGranted!!.size) {
        mSimpleCallback!!.onGranted()
      } else {
        if (!mPermissionsDenied!!.isEmpty()) {
          mSimpleCallback!!.onDenied()
        }
      }
      mSimpleCallback = null
    }
    if (mFullCallback != null) {
      if (mPermissionsRequest!!.size == 0 || mPermissions.size == mPermissionsGranted!!.size) {
        mFullCallback!!.onGranted(mPermissionsGranted)
      } else {
        if (!mPermissionsDenied!!.isEmpty()) {
          mFullCallback!!.onDenied(mPermissionsDeniedForever, mPermissionsDenied!!)
        }
      }
      mFullCallback = null
    }
    mOnRationaleListener = null
    mThemeCallback = null
  }

  private fun onRequestPermissionsResult(activity: Activity) {
    getPermissionsStatus(activity)
    requestCallback()
  }

  interface OnRationaleListener {

    fun rationale(shouldRequest: ShouldRequest)

    interface ShouldRequest {
      fun again(again: Boolean)
    }
  }

  interface SimpleCallback {
    fun onGranted()

    fun onDenied()
  }

  interface FullCallback {
    fun onGranted(permissionsGranted: MutableList<String>?)

    fun onDenied(
      permissionsDeniedForever: List<String>?,
      permissionsDenied: List<String>
    )
  }

  interface ThemeCallback {
    fun onActivityCreate(activity: Activity)
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  class PermissionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
      window.addFlags(
          WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
      )
      if (sInstance == null) {
        super.onCreate(savedInstanceState)
        Log.e("PermissionUtils", "request permissions failed")
        finish()
        return
      }
      if (sInstance!!.mThemeCallback != null) {
        sInstance!!.mThemeCallback!!.onActivityCreate(this)
      }
      super.onCreate(savedInstanceState)

      if (sInstance!!.rationale(this)) {
        finish()
        return
      }
      if (sInstance!!.mPermissionsRequest != null) {
        val size = sInstance!!.mPermissionsRequest!!.size
        if (size <= 0) {
          finish()
          return
        }
        requestPermissions(sInstance!!.mPermissionsRequest!!.toTypedArray<String>(), 1)
      }
    }

    override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<String>,
      grantResults: IntArray
    ) {
      sInstance!!.onRequestPermissionsResult(this)
      finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
      finish()
      return true
    }

    companion object {

      fun start(context: Context?) {
        val starter = Intent(context, PermissionActivity::class.java)
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(starter)
      }
    }
  }

  companion object {

    private val PERMISSIONS = permissions

    private var sInstance: PermissionUtils? = null

    /**
     * Return the permissions used in application.
     *
     * @return the permissions used in application
     */
    val permissions: List<String>
      get() = getPermissions(Utils.getApp().getPackageName())

    /**
     * Return the permissions used in application.
     *
     * @param packageName The name of the package.
     * @return the permissions used in application
     */
    fun getPermissions(packageName: String): List<String> {
      val pm = Utils.getApp().getPackageManager()
      try {
        return Arrays.asList(
            *pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                .requestedPermissions
        )
      } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        return emptyList<String>()
      }

    }

    /**
     * Return whether *you* have granted the permissions.
     *
     * @param permissions The permissions.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isGranted(vararg permissions: String): Boolean {
      for (permission in permissions) {
        if (!isGranted(permission)) {
          return false
        }
      }
      return true
    }

    private fun isGranted(permission: String): Boolean {
      return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
          Utils.getApp(), permission
      )
    }

    /**
     * Launch the application's details settings.
     */
    fun launchAppDetailsSettings() {
      val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
      intent.data = Uri.parse("package:" + Utils.getApp().getPackageName())
      Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * Set the permissions.
     *
     * @param permissions The permissions.
     * @return the single [PermissionUtils] instance
     */
    fun permission(@PermissionConstants.Permission vararg permissions: String): PermissionUtils {
      return PermissionUtils(*permissions)
    }
  }
}
