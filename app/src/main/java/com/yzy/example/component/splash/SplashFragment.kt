package com.yzy.example.component.splash


import android.Manifest
import android.util.Log
import androidx.navigation.Navigation
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.yzy.baselibrary.base.NoViewModel
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.component.comm.CommFragment
import com.yzy.example.extention.load
import com.yzy.example.imageloader.GlideApp
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : CommFragment<NoViewModel>() {

    //是否有SD卡读写权限
    private var hasSDPermission: Boolean? = null

    //倒计时是否结束
    private var countDownFinish: Boolean? = null

    override fun fillStatus(): Boolean = false
    override val contentLayout: Int = R.layout.fragment_splash

    override fun initContentView() {
        iv_sp.load("http://pic1.win4000.com/pic/7/0f/2cab03e09e.jpg")
        GlideApp.with(mActivity).load(R.drawable.aaa).into(iv_sp)
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
        if (countDownFinish == true){
//            launch(Dispatchers.Main) {
//                    delay(300)
                Navigation.findNavController(rootView).navigate(R.id.action_rootFragment_to_middleFragment)
//            }

            Log.e("fragment", this.javaClass.name+"=onRestartNavigate")
        }

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
                else -> {
                    if (!mActivity.getIsNavigate()){
                        Navigation.findNavController(rootView).navigate(R.id.action_rootFragment_to_middleFragment)
                    }
                }
            }
        }

    }
}
