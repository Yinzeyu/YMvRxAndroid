package com.yzy.pj.push;

import android.app.Application;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.mezu.MeizuRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

public class PushModel {
    private PushAgent mPushAgent;

    public static PushModel getPushModel() {
        return SingleTonHolder.sPush;
    }

    /**
     * 友盟推送初始化。
     *
     * @param application appContext
     * @param umKey       key
     * @param umSecret    secret
     * @param channelName 渠道
     */
    public void initUM(Application application, String umKey, String umSecret, String channelName) {
        Log.i("initUM", "initUM");
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(application, umKey, channelName, UMConfigure.DEVICE_TYPE_PHONE, umSecret);
        PushAgent mPushAgent = PushAgent.getInstance(application);
        getPushModel().setPushAgent(mPushAgent);
        //        openPush();
        // 应用在前台时是否显示通知
        mPushAgent.setNotificaitonOnForeground(true);
        //sdk开启通知声音
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        //默认情况下，同一台设备在1分钟内收到同一个应用的多条通知时，不会重复提醒，同时在通知栏里新的通知会替换掉旧的通知。可以通过如下方法来设置冷却时间：
        //mPushAgent.setMuteDurationSeconds(80);
        //通知栏可以设置最多显示通知的条数，当有新通知到达时，会把旧的通知隐藏。参数number可以设置为0~10之间任意整数。当参数为0时，表示不合并通知。该方法可以多次调用，以最后一次调用时的设置为准。
        mPushAgent.setDisplayNotificationNumber(1);
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知，参考http://bbs.umeng.com/thread-11112-1-1.html
        mPushAgent.setNotificationClickHandler(new NotificationClickHandler());
        mPushAgent.setMessageHandler(new MessageHandler());
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                Log.i("zhixiang", "device token: $deviceToken");
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i("zhixiang", "register failed: $s $s1");
            }
        });
    }

    /**
     * 魅族通道
     *
     * @param application appContext
     * @param mzKey       魅族key
     * @param mzSecret    魅族secret
     */
    public void initMZPush(Application application, String mzKey, String mzSecret) {
        MeizuRegister.register(application, mzKey, mzSecret);
    }

    /**
     * 小米通道
     *
     * @param application appContext
     * @param miKey       魅族key
     * @param miSecret    魅族secret
     */
    public void initMiPush(Application application, String miKey, String miSecret) {
        MiPushRegistar.register(application, miKey, miSecret);
    }

    /**
     * 华为通道
     *
     * @param application appContext
     */
    public void initHWPush(Application application) {
        HuaWeiRegister.register(application);
    }

    /**
     * OPPO Push初始化
     *
     * @param application appContext
     * @param oppoKey     Oppokey
     * @param oppoSecret  Opposecret
     */
    public void initOppoPush(Application application, String oppoKey, String oppoSecret) {
        OppoRegister.register(application, oppoKey, oppoSecret);
    }


    /**
     * vivo Push初始化
     *
     * @param application appContext
     */
    public void initVivoPush(Application application) {
        VivoRegister.register(application);
    }

    public static void closePush() {
        if (getPushModel().getPushAgent() != null) {
            getPushModel().getPushAgent().disable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    Log.i("tag", "关闭推送成功");
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.i("tag", "关闭推送失败");
                }
            });
        }
    }

    public static void openPush() {
        if (getPushModel().getPushAgent() != null) {
            getPushModel().getPushAgent().enable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    Log.i("main", "打开推送成功");
                }

                @Override
                public void onFailure(String s, String s1) {
                    Log.i("main", "打开推送失败");
                }
            });
        }
    }

    public PushAgent getPushAgent() {
        return mPushAgent;
    }

    public void setPushAgent(PushAgent pushAgent) {
        mPushAgent = pushAgent;
    }

    private static class SingleTonHolder {
        private static final PushModel sPush = new PushModel();
    }
}
