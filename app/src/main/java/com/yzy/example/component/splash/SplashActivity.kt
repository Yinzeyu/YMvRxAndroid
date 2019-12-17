package com.yzy.example.component.splash


import android.util.Log
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.gyf.immersionbar.ktx.immersionBar
import com.yzy.baselibrary.base.BaseActivity
import com.yzy.baselibrary.extention.applyFollowableSchedulers
import com.yzy.baselibrary.extention.load
import com.yzy.baselibrary.extention.mContext
import com.yzy.baselibrary.extention.toast
import com.yzy.example.R
import com.yzy.example.component.main.MainActivity
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit
import kotlin.math.max

//@Route(path = "/user/splash")
class SplashActivity : BaseActivity() {

    override fun layoutResId(): Int = R.layout.activity_splash
    //倒计时3秒
    private val count = 3L
    //倒计时
    private var disposable: Disposable? = null
    //是否有SD卡读写权限
    private var hasSDPermission: Boolean? = null
    //倒计时是否结束
    private var countDownFinish: Boolean? = null

    override fun initStatus() {
        immersionBar { statusBarDarkFont(false) }
    }

    override fun initView() {
        iv_sp.load("https://up.enterdesk.com/edpic_source/66/6d/c7/666dc7648df7e11fcd92710185610927.jpg")
        disposable?.dispose()
        //页面无缝过渡后重置背景，不然会导致页面显示出现问题。主要解决由于window背景设置后的一些问题
        window.setBackgroundDrawable(null)
        //有尺寸了才开始计时
        splashTime?.post {
            disposable = Flowable.intervalRange(0, count + 1, 0, 1, TimeUnit.SECONDS)
                    .compose(applyFollowableSchedulers())
                    .doOnNext { splashTime.text = String.format("%d", max(1, count - it)) }
                    .doOnComplete {
                        Log.e("CASE", "倒计时结束")
                        countDownFinish = true
                        goNextPage()
                    }
                    .subscribe()
        }
    }

    override fun initData() {
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(object : PermissionUtils.SimpleCallback {
                    //权限允许
                    override fun onGranted() {
                        Log.e("CASE", "有SD卡读写权限")
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
        finish()
    }

    override fun finish() {
        disposable?.dispose()
        super.finish()
    }
}
