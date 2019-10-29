package com.yzy.pj.ui.video

import android.content.Context
import com.yzy.baselibrary.extention.startActivity
import com.yzy.commonlibrary.comm.CommActivity
import com.yzy.pj.R

class PLDroidActivity : CommActivity(){
    companion object {
        fun starPLDroidActivity(context: Context) {
            context.startActivity<PLDroidActivity>()
        }
    }
    override fun layoutResId(): Int = R.layout.activity_video_pldroid

    override fun initView() {
    }

    override fun initData() {
    }


}