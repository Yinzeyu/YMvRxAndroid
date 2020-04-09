package com.yzy.example.component.main

import android.view.Gravity
import android.view.View
import androidx.databinding.ViewDataBinding
import com.yzy.example.component.playlist.PlayListFragment
import com.yzy.example.component.playlist.PlayPagerFragment
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.StringUtils
//import com.yzy.baselibrary.base.MvRxEpoxyController
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.R
import com.yzy.example.component.camera.CameraFragment
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.ffmpeg.FFmpegFragment
import com.yzy.example.component.message.ChatFragment
//import com.yzy.example.component.message.simpleTextItem
import com.yzy.example.extention.startNavigate
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : CommFragment<NoViewModel,ViewDataBinding>() {

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }

    override fun fillStatus(): Boolean = false
    private val menuList = mutableListOf(
        Pair(StringUtils.getString(R.string.chat_title), ChatFragment::class.java),
        Pair("拍摄界面", CameraFragment::class.java),
        Pair(StringUtils.getString(R.string.ffmpeg_title), FFmpegFragment::class.java),
        Pair("跳转到其他lib", Any::class.java),
        Pair(StringUtils.getString(R.string.title_play_list), PlayListFragment::class.java),
        Pair(StringUtils.getString(R.string.title_play_pager), PlayPagerFragment::class.java)
    )

    override val contentLayout: Int = R.layout.fragment_mine

    override fun initView(root: View?) {
//        mineRecycler.setController(epoxyController)
//        epoxyController.data = menuList
    }

    override fun initData() {
    }

    private var typeColor = ColorUtils.getColor(R.color.style_Primary)

    //epoxy
//    private val epoxyController =
//        MvRxEpoxyController<List<Pair<String, Class<out Any>>>> { list ->
//            list.forEachIndexed { index, pair ->
//                //内容
//                simpleTextItem {
//                    id("type_$index")
//                    msg(pair.first)
//                    textColor(typeColor)
//                    gravity(Gravity.CENTER_VERTICAL)
//                    onItemClick {
//                        when (pair.second.newInstance()) {
//                            is ChatFragment -> {
//                                startNavigate(
//                                    view,
//                                    MainFragmentDirections.actionMainFragmentToChatFragment()
//                                )
//                            }
//                            is CameraFragment -> {
//                                startNavigate(
//                                    view,
//                                    MainFragmentDirections.actionMainFragmentToCameraFragment()
//                                )
//                            }
//
//                            is FFmpegFragment -> {
//                                startNavigate(
//                                    view,
//                                    MainFragmentDirections.actionMainFragmentToFFmpegFragment()
//                                )
//                            }
//
//                            is PlayListFragment -> {
//                                startNavigate(
//                                    view,
//                                    MainFragmentDirections.actionMainFragmentToPlayListFragment()
//                                )
//                            }
//
//                            is PlayPagerFragment -> {
//                                startNavigate(
//                                    view,
//                                    MainFragmentDirections.actionMainFragmentToPlayPagerFragment()
//                                )
//                            }
//                            else -> {
//                                startNavigate(
//                                    view,
//                                    MainFragmentDirections.actionMatchToInGameNavGraph()
//                                )
//                            }
//                        }
//                    }
//                }
//                //分割线
////                dividerItem {
////                    id("line_$index")
////                }
//            }
//        }
}