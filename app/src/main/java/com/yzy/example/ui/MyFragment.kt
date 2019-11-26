package com.yzy.example.ui

import android.graphics.Color
import android.view.View
import androidx.annotation.UiThread
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SizeUtils
import com.yzy.baselibrary.extention.click
import com.yzy.baselibrary.extention.imageBitmap
import com.yzy.baselibrary.extention.load
import com.yzy.example.R
import com.yzy.example.comm.CommFragment
import kotlinx.android.synthetic.main.fragment_my.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.devilsen.czxing.code.BarcodeWriter

class MyFragment : CommFragment() {
    override val contentLayout: Int = R.layout.fragment_my

    override fun initView(root: View?) {
        myTitleClBar.layoutParams.height = SizeUtils.dp2px(128f) + BarUtils.getStatusBarHeight()
        val str =
            "https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=28b879b78b025aafc73f76999a84c001/b21bb051f8198618cfd1d0f343ed2e738bd4e637.jpg"
        roundImageView.load(str, R.drawable.icon_main_my, success = { resource ->
            resource?.let {
                riQrCenterImage.imageBitmap = it
                loadData()
            }
        }, failed = {

        })
        tvSetting.click {
        }
    }

    private fun loadData() = GlobalScope.launch() {
        val view2Bitmap = ImageUtils.view2Bitmap(rflInView)
        withContext(Dispatchers.IO) {
            val writer = BarcodeWriter()
            val bit = writer.write(
                "11111",
                SizeUtils.dp2px(212f),
                Color.BLACK, view2Bitmap
            )
            withContext(Dispatchers.Main) {
                ivQrCode.imageBitmap = bit
            }
        }


    }


    override fun initData() {
    }

    companion object {
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }

}