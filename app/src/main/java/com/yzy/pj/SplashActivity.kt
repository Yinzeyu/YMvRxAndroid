package com.yzy.pj


import android.content.Intent
import android.graphics.Color
import com.yzy.baselibrary.base.activity.BaseActivity
import com.yzy.baselibrary.extention.load
import com.yzy.baselibrary.utils.SchedulersUtil
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit


class SplashActivity : BaseActivity() {
    override fun layoutResId(): Int = R.layout.activity_splash;

    override fun initView() {
        iv_sp.load(
            "https://up.enterdesk.com/edpic_source/66/6d/c7/666dc7648df7e11fcd92710185610927.jpg",
            0
        )
    }

    override fun initDate() {
        getCheckSmsCode()
    }


    private val mCountdown = 6L
    private var mSmsDisposable: Disposable? = null

    /**
     * 撤销倒计时
     */
    private fun finishCountDown() {
        mSmsDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    private fun getCheckSmsCode() {
        finishCountDown()
        //倒计时
        mSmsDisposable = Flowable.intervalRange(1, mCountdown, 0, 1, TimeUnit.SECONDS)
            .onBackpressureBuffer()
            .compose(SchedulersUtil.applyFlowableSchedulers())
            .subscribe {
                tv_text_click.text = String.format("%d s", mCountdown - it)
                if (it == mCountdown) {
                    mSmsDisposable?.dispose()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }

}
