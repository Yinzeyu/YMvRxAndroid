package com.yzy.pj.push;

import android.app.Notification;
import android.content.Context;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * description: 消息抵达APP触发
 *
 * @author : INITIAL_D
 * @date : 2018/8/28  20:13 创建
 */

public class MessageHandler extends UmengMessageHandler {

    @Override
    public Notification getNotification(Context context, UMessage uMessage) {
        //默认为0，若填写的builder_id并不存在，也使用默认。
        return super.getNotification(context, uMessage);
    }

}
