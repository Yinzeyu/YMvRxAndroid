package com.yzy.pj.push;

import android.content.Intent;
import android.util.Log;
import com.umeng.message.UmengNotifyClickActivity;
import com.umeng.message.entity.UMessage;
import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * AndroidApp 过程rom 点击通知栏图标触发
 * version
 * Created by INITIAL_D on 2018/4/12.
 */
//https://developer.umeng.com/docs/66632/detail/66744#h1--push-88

//使用小米、华为、魅族系统通道下发的消息，将只能被统计到消息的【打开数】，而该条消息的【收到数】、【忽略数】将无法被统计到。
//若要使用小米、华为、魅族系统通道下发通知，则通知的标题（title）不允许全是空白字符且长度小于50，通知的内容（text）不允许全是空白字符且长度小于128（通知的标题和内容必填，一个中英文字符均计算为1）。

//华为弹窗功能仅支持EMUI 4.1及以上版本系统。
//对于EMUI 4.1以下版本系统，若需要使用华为系统级通道，则需在华为设备上的【手机管家】App中，开启应用的“自启动权限”。
//华为、小米、魅族对后台进程做了诸多限制。若使用一键清理，应用的channel进程被清除，将接收不到推送。为了增加推送的送达率，可选择接入华为、小米、魅族托管弹窗功能。
// 通知将由华为、小米、魅族系统托管弹出，点击通知栏将跳转到指定的Activity。
//该Activity需继承自UmengNotifyClickActivity，同时实现父类的onMessage方法，对该方法的intent参数进一步解析即可，该方法异步调用，不阻塞主线程。示例如下
public class UmengPushActivity extends UmengNotifyClickActivity {
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);
        //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.e("UmengPushActivity", "onMessage: " + body);
        UMessage msg;
        try {
            msg = new UMessage(new JSONObject(body));
            //先判断APP是否存活
            //OpenActivityHelper.openActivityByPushNotification(this,msg.custom);
            //finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
