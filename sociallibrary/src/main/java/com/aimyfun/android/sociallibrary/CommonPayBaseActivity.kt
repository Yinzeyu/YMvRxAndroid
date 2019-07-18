//package com.aimymusic.android.sociallibrary;
//
//import android.content.Intent;
//import com.aimymusic.android.commonlibrary.BuildConfig;
//import com.aimymusic.android.repositorylibrary.CommonBaseActivity;
//import com.alipay.sdk.app.EnvUtils;
//
///**
// * description: 此activity 需要使用到沙箱 支付继承   onNewIntent 微博分享回调
// *
// * @author yinzeyu
// * @date 2018/6/19 16:50
// */
//@Deprecated
//public abstract class CommonPayBaseActivity extends CommonBaseActivity {
//  @Override protected void onCreateBefore() {
//    super.onCreateBefore();
//    if (BuildConfigApp.DEBUG) {
//      EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
//    }
//  }
//
//  @Override protected void onNewIntent(Intent intent) {
//    super.onNewIntent(intent);
//    SocialApi.getSocialApi().doNewIntent(intent);
//  }
//}
