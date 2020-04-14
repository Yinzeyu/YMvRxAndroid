package com.yzy.example.component.splash


import android.Manifest
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.example.extention.load
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.component.main.MainActivity
import com.yzy.example.extention.startNavigate
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment(override val contentLayout: Int= R.layout.fragment_splash) : CommFragment<NoViewModel, ViewDataBinding>() {

    //是否有SD卡读写权限
    private var hasSDPermission: Boolean? = null

    //倒计时是否结束
    private var countDownFinish: Boolean? = null

    override fun fillStatus(): Boolean = false
    override fun initView(root: View?) {
//        hasFinish = checkReOpenHome()
//        if (hasFinish) return
        iv_sp.load("http://pic1.win4000.com/pic/7/0f/2cab03e09e.jpg")
        launch(Dispatchers.Main) {
            for (i in 5 downTo 1) {
                splashTime.text = String.format("%d", i)
                delay(1000)
            }
            countDownFinish = true
            goNextPage()
            splashTime.text = "0"
        }
    }

    override fun onRestartNavigate() {
        startNavigate(clRootView,R.id.action_splashFragment_to_mainFragment)
    }

    override fun initData() {
//        if (hasFinish) return
        if (PermissionUtils.isGranted(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            hasSDPermission = true
            goNextPage()
        } else {
            PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.SimpleCallback {
                    //权限允许
                    override fun onGranted() {
                        Log.e(
                            "CASE",
                            "有SD卡读写权限:${PermissionUtils.isGranted(PermissionConstants.STORAGE)}"
                        )
                        hasSDPermission = true
                        goNextPage()
                    }

                    //权限拒绝
                    override fun onDenied() {
                        mContext.toast("没有SD卡权限,不能使用APP")
                        hasSDPermission = false
                        goNextPage()
                    }
                })
                .request()
        }
    }

    //打开下个页面
    private fun goNextPage() {
        if (hasSDPermission == null) return
        if (countDownFinish != true) return
        if (hasSDPermission == true) {
            when {
                //是否引导
//                MMkvUtils.instance.getNeedGuide() -> GuideActivity.startActivity(mContext)
                //是否登录
//                UserRepository.instance.isLogin() -> MainActivity.startActivity(mContext)
                //没有其他需要，进入主页
                else -> MainActivity.starMainActivity(mContext)
            }
        }
        if (!isNavigate){
            startNavigate(clRootView, R.id.action_splashFragment_to_mainFragment)
        }

    }



    //https://www.cnblogs.com/xqz0618/p/thistaskroot.html
//    private fun checkReOpenHome(): Boolean {
//        // 避免从桌面启动程序后，会重新实例化入口类的activity
//        if (!this.isTaskRoot && intent != null // 判断当前activity是不是所在任务栈的根
//            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
//            && Intent.ACTION_MAIN == intent.action
//        ) {
//            finish()
//            return true
//        }
//        return false
//    }

}
