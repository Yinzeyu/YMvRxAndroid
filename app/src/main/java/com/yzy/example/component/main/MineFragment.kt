package com.yzy.example.component.main

import android.view.Gravity
import android.view.View
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.example.R
import com.yzy.example.component.camera.CameraFragment
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.comm.item.dividerItem
import com.yzy.example.component.message.ChatFragment
import com.yzy.example.component.message.simpleTextItem
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment: CommFragment(){

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }
    override fun fillStatus(): Boolean =false
    private val menuList = mutableListOf(
        Pair(StringUtils.getString(R.string.chat_title), ChatFragment::class.java),
        Pair("拍摄界面", CameraFragment::class.java)
//        Pair(StringUtils.getString(R.string.ffmpeg_title), RxFFmpegActivity::class.java),
//        Pair(StringUtils.getString(R.string.update_app), CcUpdateService::class.java),
//        Pair(StringUtils.getString(R.string.title_sticky), StickyActivity::class.java),
//        Pair(StringUtils.getString(R.string.title_drag), DragActivity::class.java),
//        Pair(StringUtils.getString(R.string.title_spedit), SpeditActivity::class.java),
//        Pair(StringUtils.getString(R.string.coordinator_refresh), CoordinatorActivity::class.java),
//        Pair(StringUtils.getString(R.string.epoxy_expandable), EpoxyExpandActivity::class.java),
//        Pair(StringUtils.getString(R.string.title_play_list), PlayListActivity::class.java),
//        Pair(StringUtils.getString(R.string.title_play_pager), PlayPagerActivity::class.java)
    )

    override val contentLayout: Int = R.layout.fragment_mine

    override fun initView(root: View?) {
        mineRecycler.setController(epoxyController)
        epoxyController.data = menuList
    }

    override fun initData() {
    }
    private var typeColor = ColorUtils.getColor(R.color.style_Primary)
    //epoxy
    private val epoxyController =
        MvRxEpoxyController<List<Pair<String, Class<out Any>>>> { list ->
            list.forEachIndexed { index, pair ->
                //内容
                simpleTextItem {
                    id("type_$index")
                    msg(pair.first)
                    textColor(typeColor)
                    gravity(Gravity.CENTER_VERTICAL)
                    onItemClick {
                        val second = pair.second.newInstance()
                        if (second is ChatFragment) {
                            ChatFragment.startChatFragment(mNavController,R.id.action_mainFragment_to_chatFragment)
                        } else if (second is CameraFragment) {
                            CameraFragment.startCameraFragment(mNavController,R.id.action_mainFragment_to_cameraFragment)
                        }

//                        else if (second is CcUpdateService) {
//                            CcUpdateService.startIntent(apkUrk, showNotification = true)
//                        }
                    }
                }
                //分割线
                dividerItem {
                    id("line_$index")
                }
            }
        }
}