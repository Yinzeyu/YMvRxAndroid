package com.yzy.pj.ui.video

import com.airbnb.epoxy.AsyncEpoxyController
import com.yzy.baselibrary.base.activity.BaseMvRxEpoxyActivity
import com.yzy.baselibrary.base.simpleController
import com.yzy.pj.GlobalLayoutUtils
import com.yzy.pj.R
import kotlinx.android.synthetic.main.activity_taste_vide.*

class TasteVideoActivity : BaseMvRxEpoxyActivity() {
    override fun epoxyController(): AsyncEpoxyController = simpleController {

    }

    override fun layoutResId(): Int = R.layout.activity_taste_vide
    override fun initView() {
        plVideoVIew.setVideoPath("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4")
        plVideoVIew.start()
        GlobalLayoutUtils(this,true)
    }

    override fun initDate() {

    }

}
