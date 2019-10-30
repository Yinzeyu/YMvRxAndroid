package com.yzy.pj.ui.video

import android.content.Context
import androidx.viewpager2.widget.ViewPager2
import com.yzy.baselibrary.extention.startActivity
import com.yzy.commonlibrary.comm.CommActivity
import com.yzy.pj.R
import kotlinx.android.synthetic.main.activity_video_pldroid.*

class PLDroidActivity : CommActivity() {
    companion object {
        fun starPLDroidActivity(context: Context) {
            context.startActivity<PLDroidActivity>()
        }
    }

    var plAdapter: PlAdapter? = null
    override fun layoutResId(): Int = R.layout.activity_video_pldroid

    override fun initView() {
        plAdapter = PlAdapter()
        plListView.adapter = plAdapter
        plListView.orientation = ViewPager2.ORIENTATION_VERTICAL
        plListView.getChildAt(0).overScrollMode = ViewPager2.OVER_SCROLL_NEVER
        var list = mutableListOf<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        plAdapter?.addList(list)
    }

    override fun initData() {
    }


}