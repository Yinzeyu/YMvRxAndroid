package com.yzy.example.component.album

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyAdapter
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.Utils.runOnUiThread
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.baselibrary.extention.*
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.comm.item.DividerItem_
import com.yzy.example.component.comm.item.dividerItem
import com.yzy.example.component.main.MainActivity
import com.yzy.example.extention.options
import com.yzy.example.repository.ViewModelFactory
import com.yzy.example.repository.bean.AlbumBean
import com.yzy.example.utils.CameraUtils
import com.yzy.example.utils.album.MediaType
import com.yzy.example.utils.album.entity.LocalMedia
import com.yzy.example.utils.album.entity.LocalMediaFolder
import com.yzy.example.widget.GridItemDecoration
import kotlinx.android.synthetic.main.fragment_album.*
import java.io.File
import java.util.*

class AlbumFragment : CommFragment() {

    private val videoMinDur: Long by lazy {
        arguments?.getLong("videoMinDur") ?: (3 * 1000).toLong()
    }

    private val videoMaxDur: Long by lazy {
        arguments?.getLong("videoMaxDur") ?: (100 * 1000).toLong()
    }
    //最多图片选择数量
    private val maxPicSize: Int by lazy {
        arguments?.getInt("maxPicSize") ?: 9
    }

    //是否需要混合选择
    private val isMixing: Boolean by lazy {
        arguments?.getBoolean("isMixing") ?: false
    }
    //是否只选择一张图
    private val isOnlyPic: Boolean by lazy {
        arguments?.getBoolean("isOnlyPic") ?: false
    }

    private val isOnlyOne: Boolean by lazy {
        arguments?.getBoolean("isOnlyOne") ?: false
    }
    //是否需要裁切
    private val isNeedCut: Boolean by lazy {
        arguments?.getBoolean("isNeedCut") ?: false
    }
    //是否裁切为正方形,否则16:9
    private val needCutSquare: Boolean by lazy {
        arguments?.getBoolean("needCutSquare") ?: false
    }
    //是否显示圆形的蒙层，只有当square=tue才生效
    private val needCutLayerCircle: Boolean by lazy {
        arguments?.getBoolean("needCutLayerCircle") ?: false
    }
    private var isOpen = false
    private var animRecycler: ObjectAnimator? = null
    private var animArrow: ObjectAnimator? = null
    private var animOver: ObjectAnimator? = null
    private var dirAdapter: DirAdapter? = null
    private var imgAdapter: ImgAdapter? = null
    private val mViewModel: PicSelectViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelFactory()
        ).get(PicSelectViewModel::class.java)
    }

    companion object {
        fun startAlbumFragment(
            controller: NavController, @IdRes id: Int,
            isMixing: Boolean = false,
            videoMinDur: Long = 3 * 1000,
            videoMaxDur: Long = 100 * 1000,
            maxPicSize: Int = 9,
            isOnlyOne: Boolean = true,
            isOnlyPic: Boolean = false,
            isNeedCut: Boolean = true,
            needCutSquare: Boolean = true,
            needCutLayerCircle: Boolean = false
        ) {
            controller.navigate(id, Bundle().apply {
                putBoolean("isMixing", isMixing)
                putBoolean("isOnlyOne", isOnlyOne)
                putBoolean("isOnlyPic", isOnlyPic)
                putBoolean("isNeedCut", isNeedCut)
                putBoolean("needCutSquare", needCutSquare)
                putBoolean("needCutLayerCircle", needCutLayerCircle)
                putInt("maxPicSize", maxPicSize)
                putLong("videoMinDur", videoMinDur)
                putLong("videoMaxDur", videoMaxDur)
            }, options)
        }
    }

    override val contentLayout: Int = R.layout.fragment_album
    override fun initView(root: View?) {
        flTitleBarView.layoutParams.height = SizeUtils.dp2px(44f) + BarUtils.getStatusBarHeight()
        picSelTitleBack.pressEffectAlpha()
        picSelTitleTxt.pressEffectAlpha()
        picSelTitleConfirm.pressEffectAlpha()
        picSelTitleTxt.text = "所有照片"
        picSelTitleBack.click { if (isOpen) changeDirState() else mContext.onBackPressed() }
        picSelTitleTxt.click { changeDirState() }
        picSelTitleArrow.click { changeDirState() }
        picSelTitleConfirm.visibility = if (isOnlyOne) View.GONE else View.VISIBLE
        mViewModel.loadAllDirs(
            activity = (mContext as MainActivity),
            onlyImg = isOnlyPic,
            mindur = videoMinDur,
            maxdur = videoMaxDur,
            mixing = isMixing
        )
        picSelOver.click { if (isOpen) changeDirState() }
        dirAdapter = DirAdapter()
        picDirRecycler.adapter = dirAdapter
        picDirRecycler.layoutManager = LinearLayoutManager(mContext)
        val dec = GridItemDecoration(mContext)
        dec.setHorizontalDivider(ContextCompat.getDrawable(mContext, R.drawable.divider_horizontal))
        dec.setVerticalDivider(ContextCompat.getDrawable(mContext, R.drawable.divider_vertical))
        dec.setNeedDraw(true)
        picSelRecycler.addItemDecoration(dec)
        imgAdapter = ImgAdapter()
        picSelRecycler.adapter = imgAdapter
        picSelRecycler.layoutManager = GridLayoutManager(mContext, 4)
        picSelOver.alpha = 0f
        picSelTitleArrow.post {
            picDirRecycler.post {
                picSelTitleArrow.pivotX =
                    (picSelTitleArrow.width - picSelTitleArrow.paddingEnd) / 2f
                picSelTitleArrow.pivotY = picSelTitleArrow.height / 2f
                picDirRecycler.translationY = -picDirRecycler.height.toFloat()
                picDirRecycler.visible()
            }
        }
    }

    override fun initData() {
        mViewModel.uiState.observe(this, androidx.lifecycle.Observer {
            it?.let {
                if (it.data.size > it.selectIndex) {
                    picSelTitleTxt.text = it.data[it.selectIndex].name
                }
                //选中数量
                if (mViewModel.getSelectMedias().size > 0) {
                    picSelTitleConfirm.isEnabled = true
                    picSelTitleConfirm.alpha = 1f
                } else {
                    picSelTitleConfirm.isEnabled = false
                    picSelTitleConfirm.alpha = 0.2f
                }
                dirAdapter?.setData(it.data, it.selectIndex)
                imgAdapter?.setData(
                    it,
                    mContext,
                    maxPicSize,
                    mViewModel,
                    isOnlyOne,
                    isMixing,
                    isNeedCut,
                    mOnCameraListener, callBack = {

                    },
                    cutCallBack = { mediaTemp ->
                        mCutMedia = mediaTemp
                        mediaTemp.path?.let { path ->
                            PicCutFragment.startPicCutFragment(
                                mNavController,
                                R.id.action_albumFragment_to_picCutFragment,
                                path,
                                needCutSquare,
                                needCutLayerCircle
                            )
                        }

                    }
                )
            }
        })
        dirAdapter?.selectClick = {
            mViewModel.loadPicsInDir(it)
            changeDirState()
        }
    }

    class DirAdapter : EpoxyAdapter() {
        var selectClick: ((String) -> Unit)? = null
        fun setData(data: MutableList<LocalMediaFolder>, selectIndex: Int) {
            removeAllModels()
            //文件夹遍历
            data.forEachIndexed { index, dir ->
                dir.isChecked = index == selectIndex
                addModels(PicDirItem_().apply {
                    id(dir.path + "_dir_" + (dir.checkedNum))
                    mediaDir(dir)
                    onItemClick {
                        selectClick?.invoke(dir.path ?: "")

                    }
                })
                addModels(DividerItem_().apply {
                    id(dir.path + "_dir_line")
                    leftMarginDp(16f)
                    rightMarginDp(16f)
                })
            }

        }
    }

    class ImgAdapter : EpoxyAdapter() {
        fun setData(
            albumBean: AlbumBean,
            mContext: Context,
            maxPicSize: Int,
            mViewModel: PicSelectViewModel,
            isOnlyOne: Boolean,
            isMixing: Boolean,
            isNeedCut: Boolean,
            mOnCameraListener: CameraUtils.OnCameraListener,
            callBack: (() -> Unit)? = null,
            cutCallBack: ((media: LocalMedia) -> Unit)? = null
        ) {
            removeAllModels()
            if (albumBean.selectIndex == 0) {
                //添加添加item
                addModels(PicCameraItem_().apply {
                    id("0_itemCamera")
                    onItemClick {
                        if (mViewModel.getSelectMedias().size >= maxPicSize) {
                            mContext.toast("最多只能选择${maxPicSize}张")
                        } else {
                            if (CameraUtils.mCameraListener != mOnCameraListener) {
                                CameraUtils.mCameraListener = mOnCameraListener
                            }
                            CameraUtils.requestPermissionCamera(mContext as MainActivity, false)
                        }
                    }
                }.spanSizeOverride { _, _, _ -> 1 })

            }
            //防止手机没有图片
            if (albumBean.data.size > albumBean.selectIndex) {
                //文件遍历
                albumBean.data[albumBean.selectIndex].images?.let { imgs ->
                    imgs.forEachIndexed { index, media ->
                        addModels(PicImgItem_().apply {
                            id(albumBean.selectIndex.toString() + media.path)
                            onlySelOne(isOnlyOne)
                            mixing(isMixing)
                            localMedia(media)
                            checkedIndex(
                                if (imgs[index].isChecked) {
                                    mViewModel.getSelectMedias().indexOfFirst {
                                        TextUtils.equals(
                                            imgs[index].path,
                                            it.path
                                        )
                                    } + 1
                                } else 0)
                            onItemClick { mediaTemp ->
                                if (mediaTemp.isVideo) {//视频
                                    if (isNeedCut) {
                                        mContext.toast("进入视频裁切页面")
                                    } else {
                                        if (mViewModel.getSelectMedias().size > 0 && !isMixing) {
                                            mContext.toast("照片和视频不能同时选择")
                                            return@onItemClick
                                        }
                                        val list: ArrayList<LocalMedia> = arrayListOf()
                                        list.add(mediaTemp)
                                        PicTempLiveData.setSelectPicList(mViewModel.getSelectMedias())
//                                        PicComponent.startPicSee(list, maxPicSize, isMixing)
                                    }
                                } else {//图片
                                    if (isOnlyOne && isNeedCut) {
                                        cutCallBack?.invoke(mediaTemp)
                                    } else if (isOnlyOne && !isNeedCut) {
                                        val list: MutableList<LocalMedia> = mutableListOf()
                                        list.add(mediaTemp)
                                        PicSelLiveData.setSelectPicList(list)
                                        callBack?.invoke()
                                    } else {
                                        PicTempLiveData.setSelectPicList(mViewModel.getSelectMedias())
                                        PicPagerLiveData.setPagerListData(index, imgs)
//                                        PicComponent.startPicSee(null, maxPicSize, isMixing)
                                    }
                                }
                            }
                            onClickCheck { _, view ->
                                val mediaTemp = imgs[index]
                                //选择的是视频
                                if (mediaTemp.isVideo) {
                                    if (mViewModel.getSelectMedias().size > 0 && !isMixing) {
                                        mContext.toast("照片和视频不能同时选择")
                                    } else if (!isNeedCut) {
                                        if (mViewModel.getSelectMedias().size >= maxPicSize && !mediaTemp.isChecked) {
                                            mContext.toast("最多只能选择${maxPicSize}个")
                                        } else if (isMixing) {
                                            startAnim(mediaTemp.isChecked, view)
                                            val newMedia =
                                                mediaTemp.copy(isChecked = !mediaTemp.isChecked)
                                            Collections.replaceAll(imgs, mediaTemp, newMedia)
                                            mViewModel.changeMediaChecked(
                                                albumBean.selectIndex,
                                                newMedia
                                            )
                                            notifyDataSetChanged()
                                        } else {
                                            val list: MutableList<LocalMedia> = mutableListOf()
                                            list.add(mediaTemp)
                                            PicSelLiveData.setSelectPicList(list)
                                            callBack?.invoke()
                                        }
                                    } else {
                                        //TODO
                                        mContext.toast("进入视频裁切页面")
                                        return@onClickCheck
                                    }
                                } else {
                                    if (mViewModel.getSelectMedias().size >= maxPicSize && !mediaTemp.isChecked) {
                                        mContext.toast("最多只能选择$maxPicSize${if (isMixing) "个" else "张"}")
                                    } else {
                                        startAnim(mediaTemp.isChecked, view)
                                        val newMedia =
                                            mediaTemp.copy(isChecked = !mediaTemp.isChecked)
                                        imgs[index] = newMedia
                                        mViewModel.changeMediaChecked(
                                            albumBean.selectIndex,
                                            newMedia
                                        )
                                        notifyDataSetChanged()
                                    }
                                }
                            }
                        }.spanSizeOverride { _, _, _ -> 1 })

                    }
                }
            }
        }

        private fun startAnim(
            oldHasChecked: Boolean,
            view: View
        ) {
            val iv: View? = view.findViewById(R.id.itemPicCheckIv)
            val tv: View? = view.findViewById(R.id.itemPicCheckTv)
            if (iv != null && tv != null) {
                if (oldHasChecked) {
                    iv.clearAnimation()
                    tv.clearAnimation()
                    iv.scaleX = 1f
                    iv.scaleY = 1f
                    tv.scaleX = 1f
                    tv.scaleY = 1f
                } else {
                    iv.scaleX = 0.8f
                    iv.scaleY = 0.8f
                    tv.scaleX = 0.8f
                    tv.scaleY = 0.8f
                    iv.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .start()
                    tv.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .start()
                }
            }
        }
    }

    private val mOnCameraListener = object : CameraUtils.OnCameraListener {
        override fun onSuccess() {
            CameraUtils.openCamera(mContext as MainActivity)
        }

        override fun onAlbumPath(urlPath: String?) {
        }

        override fun onError() {
            mContext.toast("拍照出错")
        }
    }
    //需要裁切的图片信息
    private var mCutMedia: LocalMedia? = null

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (CameraUtils.isFromCapture(requestCode)) {
            CameraUtils.getTakePhotoFilePath(requestCode, resultCode)?.let {
                //                mContext.sendBroadcast(
//                    Intent(
//                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                        Uri.fromFile(File(it))
//                    )
//                )
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                options.inSampleSize = 1
                BitmapFactory.decodeFile(it, options)
                val localMedia = LocalMedia(
                    path = it,
                    width = options.outWidth,
                    height = options.outHeight,
                    isChecked = !isOnlyOne,
                    mimeType = MediaType.IMAGE.ordinal
                )
                if (isOnlyOne) {
                    if (isNeedCut) {
                        mCutMedia = localMedia
                        PicCutFragment.startPicCutFragment(
                            mNavController,
                            R.id.action_albumFragment_to_picCutFragment,
                            it,
                            needCutSquare,
                            needCutLayerCircle
                        )
                    } else {
                        val list: MutableList<LocalMedia> = mutableListOf()
                        list.add(localMedia)
                        PicSelLiveData.setSelectPicList(list)
                        mContext.onBackPressed()
                    }
                } else {
                    mViewModel.andAddCameraMedia(localMedia)
                }
            }
        }
    }

    fun cutResultUrl(path: String) {
        val list: MutableList<LocalMedia> = mutableListOf()
        mCutMedia?.let { media ->
            media.cutPath = path
            list.add(media)
            PicSelLiveData.setSelectPicList(list)
            mContext.onBackPressed()

        }

    }

    private fun changeDirState() {
        isOpen = !isOpen
        animRecycler = ObjectAnimator.ofFloat(
            picDirRecycler, "translationY", picDirRecycler.translationY,
            if (isOpen) 0f else -picDirRecycler.height.toFloat()
        )
        animArrow = ObjectAnimator.ofFloat(
            picSelTitleArrow, "rotation", picSelTitleArrow.rotation, if (isOpen) 180f else 0f
        )
        picSelOver.visibility = View.VISIBLE
        animOver = ObjectAnimator.ofFloat(
            picSelOver, "alpha", picSelOver.alpha, if (isOpen) 1f else 0f
        )
        animOver?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                picSelOver?.let {
                    if (!isOpen) {
                        it.visibility = View.GONE
                        it.alpha = 0f
                    } else {
                        it.alpha = 1f
                    }
                }
            }
        })
        animRecycler?.start()
        animArrow?.start()
        animOver?.start()
    }
}
