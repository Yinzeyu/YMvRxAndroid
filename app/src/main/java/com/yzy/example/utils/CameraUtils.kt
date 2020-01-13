package com.yzy.example.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.Utils
import com.yzy.baselibrary.extention.toast
import com.yzy.example.component.main.MainActivity
import com.yzy.example.utils.permisstions.InlineRequestPermissionException
import com.yzy.example.utils.permisstions.requestPermissionsForResult
import com.yzy.example.widget.file.AppFileDirManager
import com.yzy.example.widget.file.FilePathConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * description:
 *
 * @author yinzeyu
 * @date 2018/7/6 14:34
 */
object CameraUtils {

  /**
   * 拍照得到的文件
   */
  var fileTakePhoto: File? = null
    private set

  private var isCutImage = true
  /**
   * 拍照的请求码
   */
  val REQUEST_CAPTURE = 0x0111

  /**
   * 监听 请求权限成功处理不同事情
   *
   * @param onCameraListener 监听事件
   */
  var mCameraListener: OnCameraListener? = null

  interface OnCameraListener {
    fun onSuccess()

    fun onAlbumPath(urlPath: String?)

    fun onError()
  }

  /**
   * 获取相机内权限
   *
   * @param activity 传入当前activity
   * @param cutImage 需要裁切图片
  */
  fun requestPermissionCamera(activity: MainActivity, cutImage: Boolean) {
    requestPermissionCamera(activity)
    isCutImage = cutImage
  }
  private val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
  /**
   * 获取相机内权限
   *
   * @param activity 传入当前activity
   */
  @SuppressLint("CheckResult")
  fun requestPermissionCamera(activity: MainActivity) {
    CoroutineScope(Dispatchers.Main).launch {
      try {
        activity.requestPermissionsForResult(*permissions)
        mCameraListener?.onSuccess()
      }catch (e: InlineRequestPermissionException){
        mCameraListener?.onError()
      }
    }
  }

  /**
   * 提示权限设置弹窗
   */
  private fun showDialogPermissionSettings(activity: Activity) {
    activity.toast("相机启动失败！请去应用权限里为咪哒开放相机权限，或检查是否相机被其他应用占用。")
//    CommDialog.builder(activity)
//      .setTitleStr("提示")
//      .setContentStr("相机启动失败！请去应用权限里为咪哒星球开放相机权限，或检查是否相机被其他应用占用。")
//      .setCancelStr("取消")
//      .setConfirmStr("去设置")
//      .setConfirmClick {
//        PermissionUtils.gotoPermissionSetting(activity)
//      }
//      .build()
//      .show()
  }

  fun openCamera(activity: MainActivity) {

    try {
      //targetSdkVersion低于23，使用异常捕捉相机权限
      val camera = Camera.open()
      camera.release()
      setCamera(activity)
    } catch (e: Exception) {
      showDialogPermissionSettings(activity)
    }

  }

  /**
   * 打开系统相机
   */
  private fun setCamera(mContext: MainActivity) {
    fileTakePhoto = File(
      AppFileDirManager.getAppFileDir(mContext) + FilePathConstants.IMAGES,
      System.currentTimeMillis().toString() + ".jpg"
    )
    fileTakePhoto?.let {
      if (!it.parentFile.exists()) {
        Log.i("CameraUtils", "拍照文件夹是否创建成功" + it.parentFile.mkdirs())
      }
      val imageUri: Uri
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //通过FileProvider创建一个content类型的Uri
        imageUri = FileProvider.getUriForFile(
          mContext, FileProviderUtils.instance.PATH, it
        )
      } else {
        imageUri = Uri.fromFile(fileTakePhoto)
      }
      val intent = Intent()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      }
      //设置Action为拍照
      intent.action = MediaStore.ACTION_IMAGE_CAPTURE
      //将拍取的照片保存到指定URI
      intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
      mContext.startActivityForResult(intent, REQUEST_CAPTURE)
    }
  }

  fun onActivityResult(
    requestCode: Int, resultCode: Int, data: Intent?, activityName: String,
    context: Context
  ) {
    fileTakePhoto?.let {
      if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
        if (mCameraListener != null && fileTakePhoto != null) {
          if (it.exists()) {
            val uri=Uri.fromFile(it)
            Utils.getApp()
              .sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri ))
            if (isCutImage) {
              mCameraListener?.onAlbumPath(it.path)
            } else {
              mCameraListener?.onAlbumPath(it.path)
            }
          } else {
            mCameraListener?.onAlbumPath(null)
          }
        }
      } else if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_CANCELED) {
        if (mCameraListener != null) {
          mCameraListener?.onAlbumPath(null)
        }
      }
      isCutImage = true
    }
  }

  /**
   * 是否从拍照跳转返回的
   */
  fun isFromCapture(requestCode: Int): Boolean {
    return requestCode == REQUEST_CAPTURE
  }

  /**
   * 获取拍照
   */
  fun getTakePhotoFilePath(requestCode: Int, resultCode: Int): String? {
    if (isFromCapture(requestCode) && resultCode == RESULT_OK) {
      return fileTakePhoto?.path
    }
    return null
  }

  fun clearListener() {
    mCameraListener?.let {
      mCameraListener = null
    }
  }
}
