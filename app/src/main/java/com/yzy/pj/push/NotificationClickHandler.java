package com.yzy.pj.push;

import android.content.Context;

import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * description: 友盟消息回掉类 点击执行
 *
 * @author : yinzeyu
 * @date : 2018/8/28  20:13 创建
 */

public class NotificationClickHandler extends UmengNotificationClickHandler {

    @Override
    public void dealWithCustomAction(Context context, UMessage uMessage) {
        String msg = uMessage.custom;
        //如果APP在后台
//    if (BaseApplication.INSTANCE.getAppManager().getActivityList().size() > 0) {
//      //如果用戶已经登录，直接走分发逻辑
//      if (UserManager.Companion.getInstance().getUserBean() != null) {
//        OpenActivityManager.Companion.startMainActivity(context);
//        OpenActivityManager.Companion.initPush(context, msg);
//        return;
//      }
//      //没有登录则跳转到登录页
//      UserModule.Companion.startLoginActivity(context);
//      return;
//    }
//    if (GameGlobal.INSTANCE.isGaming()) {
//      GameModule.Companion.restartCCGameActivity(context, GameModule.GAME_FROM_MAIN, -1);
//    } else {
//      SplashActivity.Companion.startActivity(context, msg, true, false);
//    }
    }
}
