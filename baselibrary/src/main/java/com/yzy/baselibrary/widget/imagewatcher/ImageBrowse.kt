package com.yzy.baselibrary.widget.imagewatcher

import android.net.Uri
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import java.util.*
import kotlin.math.min


/**
 * description : 查看大图
 *
 * @author : case
 * @date : 2018/8/7 21:46
 */
class ImageBrowse private constructor() {
    private var mWatcher: ImageWatcher? = null

    fun show(iv: ImageView?, viewList: List<ImageView?>?, urlList: List<String>?) {
        if (mWatcher != null && iv != null && viewList != null && urlList != null) {
            val array = SparseArray<ImageView?>()
            val size = min(viewList.size, urlList.size)
            for (i in viewList.indices) {
                array.put(i, viewList[i])
                if (array.size() == size) {
                    break
                }
            }
            val uris = ArrayList<Uri>()
            for (s in urlList) {
                uris.add(Uri.parse(s))
                if (uris.size == size) {
                    break
                }
            }
            mWatcher?.show(iv, array, uris)
        }
    }

    companion object {
        fun newInstance(
            activity: FragmentActivity,
            listener: EnterExitListener?,
            isShowIndex: Boolean, logClickListener: ((v: View, uri: Uri, pos: Int) -> Unit)? = null
        ): ImageBrowse {
            val imageBrowse = ImageBrowse()
            val imageWatcherHelper = ImageWatcherHelper.with(activity)
            if (isShowIndex) {
                imageWatcherHelper.setIndexProvider(FunIndexProvider())
            }
            imageBrowse.mWatcher =
                imageWatcherHelper.setTranslucentStatus(0)
                    .setLoader(GlideLoader())
                    .setOnStateChangedListener(listener)
                    .setLoadingUIProvider(LoadingProvider())
                    .setOnPictureLongPressListener { v, uri, pos ->
                        logClickListener?.invoke(v, uri, pos)
                    }
                    .create()
            return imageBrowse
        }

    }
}
