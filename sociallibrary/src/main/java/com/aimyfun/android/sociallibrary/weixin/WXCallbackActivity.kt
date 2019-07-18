package com.aimyfun.android.sociallibrary.weixin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.aimyfun.android.sociallibrary.PlatformConfig
import com.aimyfun.android.sociallibrary.PlatformType
import com.aimyfun.android.sociallibrary.SocialApi
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * description:  微信登陆分享 activity
 *
 * @author yinzeyu
 * @date 2018/6/16 19:16
 */
class WXCallbackActivity : Activity(), IWXAPIEventHandler {

  protected var mWXHandler: WXHandler? = null
  protected var mWXCircleHandler: WXHandler? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    this.mWXHandler = SocialApi.socialApi().getSSOHandler(PlatformType.WEIXIN) as WXHandler?
    this.mWXHandler!!.onCreate(
        this.getApplicationContext(),
        PlatformConfig.getPlatformConfig(PlatformType.WEIXIN)
    )

    this.mWXCircleHandler =
        SocialApi.socialApi().getSSOHandler(PlatformType.WEIXIN_CIRCLE) as WXHandler?
    this.mWXCircleHandler!!.onCreate(
        this.getApplicationContext(),
        PlatformConfig.getPlatformConfig(PlatformType.WEIXIN_CIRCLE)
    )

    this.mWXHandler!!.wxApi!!.handleIntent(this.intent, this)
  }

  override fun onNewIntent(paramIntent: Intent) {
    super.onNewIntent(paramIntent)
    this.mWXHandler = SocialApi.socialApi().getSSOHandler(PlatformType.WEIXIN) as WXHandler?
    this.mWXHandler!!.onCreate(
        this.getApplicationContext(),
        PlatformConfig.getPlatformConfig(PlatformType.WEIXIN)
    )

    this.mWXCircleHandler =
        SocialApi.socialApi().getSSOHandler(PlatformType.WEIXIN_CIRCLE) as WXHandler?
    this.mWXCircleHandler!!.onCreate(
        this.getApplicationContext(),
        PlatformConfig.getPlatformConfig(PlatformType.WEIXIN_CIRCLE)
    )

    this.mWXHandler!!.wxApi!!.handleIntent(this.intent, this)
  }

  override fun onResp(resp: BaseResp?) {
    if (this.mWXHandler != null && resp != null) {
      try {
        this.mWXHandler!!.wxEventHandler!!.onResp(resp)
      } catch (var3: Exception) {
      }

    }

    if (this.mWXCircleHandler != null && resp != null) {
      try {
        this.mWXCircleHandler!!.wxEventHandler!!.onResp(resp)
      } catch (var3: Exception) {
      }

    }

    this.finish()
    overridePendingTransition(0, 0)
  }

  override fun onReq(req: BaseReq) {
    if (this.mWXHandler != null) {
      this.mWXHandler!!.wxEventHandler!!.onReq(req)
    }

    this.finish()
    overridePendingTransition(0, 0)
  }
}