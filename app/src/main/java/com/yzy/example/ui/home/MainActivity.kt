package com.yzy.example.ui.home

import android.content.Context
import androidx.annotation.IntRange
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.extention.*
import com.yzy.commonlibrary.comm.CommActivity
import com.yzy.commonlibrary.comm.CommFragment
import com.yzy.example.R
import com.yzy.example.ui.IndexFragment
import com.yzy.example.ui.elephant.ViewPager2Activity
import com.yzy.example.ui.initAddFriendDialog
import com.yzy.example.ui.video.TasteVideoActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.devilsen.czxing.Scanner
class MainActivity : CommActivity() {
    companion object {
        fun starMainActivity(context: Context) {
            context.startActivity<MainActivity>()
        }
    }

    //当前页面
    private var currentFragment: CommFragment? = null
    //子列表合集，方便外部调用选中那个
    private var fragmentList = mutableListOf<CommFragment>()

    override fun layoutResId(): Int = R.layout.activity_main
    override fun initView() {
    }

    override fun initData() {
        val defaultInfo = "屏幕宽度:$mScreenWidth\n屏幕高度:$mScreenHeight\n状态栏高度:$mStatusBarHeight"
        //键盘监听
        addListerKeyboard(naHeight = {
            LogUtils.e(
                    "contentHeight" + String.format(
                            "%s\n虚拟导航键高度:%d\n键盘高度:%d",
                            defaultInfo,
                            it,
                            getHeightKeyboard()
                    )
            )
        }, keyboardHeight = {
            LogUtils.e(
                    "contentHeight" + String.format(
                            "%s\n虚拟导航键高度:%d\n键盘高度:%d",
                            defaultInfo,
                            getHeightNavigationBar(),
                            it
                    )
            )
        })


        fragmentList = mutableListOf(IndexFragment.newInstance())
        //设置选中
        selectFragment(0)

        flMainElephant.click {
            ViewPager2Activity.starElephantActivity(mContext)
        }

        flScanView.click {
            Scanner.with(mContext).setOnScanResultDelegate { _, _ ->

            }.start()
        }
        flPlay.click {
            TasteVideoActivity.starTasteVideoActivity(mContext)
        }
        flShowDialog.click {
            initAddFriendDialog(supportFragmentManager) {

            }
        }

        flDoubleApi.click {
            GankActivity.starGankActivity(mContext)
        }

        flDoublePlay.click {
//            PLDroidActivity.starPLDroidActivity(mContext)
//            XRouter.with(mContext).target("www.baidu.com").jump()
        }


    }

    //设置选中的fragment
    private fun selectFragment(@IntRange(from = 0, to = 2) index: Int) {
        //需要显示的fragment
        val fragment = fragmentList[index]
        //和当前选中的一样，则不再处理
        if (currentFragment == fragment) return
        //先关闭之前显示的
        currentFragment?.let { FragmentUtils.hide(it) }
        //设置现在需要显示的
        currentFragment = fragment
        if (!fragment.isAdded) { //没有添加，则添加并显示
            val tag = fragment::class.java.simpleName
            FragmentUtils.add(supportFragmentManager, fragment, mainContainer.id, tag, false)
        } else { //添加了就直接显示
            FragmentUtils.show(fragment)
        }
    }

    private var touchTime = 0L
    private val waitTime = 2000L
    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - touchTime >= waitTime) {
            //让Toast的显示时间和等待时间相同
            toast(R.string.double_exit)
            touchTime = currentTime
        } else {
//      AppUtils.exitApp()
            super.onBackPressed()
        }
    }
}
