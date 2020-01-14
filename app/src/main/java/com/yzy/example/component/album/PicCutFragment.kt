package com.yzy.example.component.album

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.NavController
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.component.comm.CommTitleFragment
import com.yzy.example.extention.options
import com.yzy.example.utils.PathUtils
import com.yzy.example.widget.crop.ucrop.view.SimpleTransformImageListener
import kotlinx.android.synthetic.main.activity_comm_title.*
import kotlinx.android.synthetic.main.fragment_pic_cut.picCutUcrop
import kotlinx.android.synthetic.main.layout_comm_title.*
import java.io.File

/**
 * Description: 图片裁切页
 * @author: yzy
 * @date: 19-5-24 下午2:08
 */
class PicCutFragment : CommTitleFragment() {
    //需要裁切的图片地址
    var imageUrl: String = ""
    //是否裁切为正方形,否则16:9
    var square: Boolean = false
    //是否显示圆形的蒙层，只有当square=tue才生效
    var layerCircle: Boolean = false

    companion object {
        fun startPicCutFragment(
            controller: NavController, @IdRes id: Int,
            imageUrl: String,
            square: Boolean,
            layerCircle: Boolean

        ) {
            controller.navigate(id, Bundle().apply {
                putString("imageUrl", imageUrl)
                putBoolean("square", square)
                putBoolean("layerCircle", layerCircle)

            }, options)
        }
    }

    override fun layoutResContentId(): Int {
        return R.layout.fragment_pic_cut
    }

    override fun initContentView() {
        flTitleBarView.layoutParams.height = SizeUtils.dp2px(44f) + BarUtils.getStatusBarHeight()
        imageUrl = arguments?.getString("imageUrl") ?: ""
        square = arguments?.getBoolean("square") ?:false
        layerCircle = arguments?.getBoolean("layerCircle") ?:false

        setTitleText("裁剪")
        setTitleRightTv("完成")
        setTitleRightTv {
            showActionLoading("裁剪中")
            picCutUcrop.cropAndSaveImage(Bitmap.CompressFormat.PNG, 90) { result ->
                dismissActionLoading()
                if (result != null && result.isSuccessful && result.uri != null) {
                    val intent = Intent()
                    intent.putExtra("key_pic_cut_result", result.uri.path)
//          setResult(RESULT_OK, intent)
//          finish()
                } else {
                    Log.e("cut_image", "图片裁切失败：" + if (result == null) "" else result.error)
                    mContext.toast("图片裁切失败")
                }
            }
        }
    }

    override fun initData() {
//    imageUrl = intent.getStringExtra(RouterConstants.Pic.KEY_PIC_CUT_URL)
//    square = intent.getBooleanExtra(RouterConstants.Pic.KEY_PIC_CUT_SQUARE, true)
//    layerCircle = intent.getBooleanExtra(RouterConstants.Pic.KEY_PIC_CUT_LAYER_CIRCLE, false)
        //手势
        val mGestureCropImageView = picCutUcrop.cropImageView
        mGestureCropImageView.isScaleEnabled = true
        mGestureCropImageView.isRotateEnabled = false
        //裁切比例
        mGestureCropImageView.targetAspectRatio = if (square) 1f else 16f / 9
        //蒙层
        val mOverlayView = picCutUcrop.overlayView
        mOverlayView.setCircleDimmedLayer(square && layerCircle)
        mOverlayView.setShowCropGrid(false)
        mOverlayView.setShowCropFrame(false)
        //图片加载监听，防止不能裁切
        mGestureCropImageView.setTransformImageListener(
            object : SimpleTransformImageListener() {
                override fun onLoadFailure(e: Exception) {
                    picCutUcrop.visibility = View.INVISIBLE
                    commonTitleRightTv.alpha = 0.2f
                    commonTitleRightTv.isEnabled = false
                    mContext.toast("暂不支持此类图片裁切")
                }
            })
        //最好每次打开APP进行一次图片裁切的清理
        val fileParent = File(PathUtils.getInstance().getDiskCacheDir(mContext), "aimyImgCut")
        if (!fileParent.exists()) fileParent.mkdirs()
        //裁切的图片名称
        val outPutImgName = System.currentTimeMillis().toString()
        //裁切出来的文件地址
        val outputUri = Uri.fromFile(File(fileParent, outPutImgName))
        //加载图片
        try {
            val imageUri = Uri.fromFile(File(imageUrl))
            picCutUcrop.setImageUri(imageUri, outputUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}