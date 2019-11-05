package com.yzy.example.imModel

import android.content.Context
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.yzy.baselibrary.app.BaseApplication
import com.yzy.baselibrary.extention.toast
import com.yzy.example.IMLoginGlobal
import com.yzy.example.im.IM
import com.yzy.example.im.IMConstant
import com.yzy.example.im.IMInit
import com.yzy.example.im.entity.IMActionEntity

/**
 *description: Im的工具类.
 *@date 2019/5/20 14:53.
 *@author: YangYang.
 */
object IMUtils {

    private var isIMLogin = false

    fun init(app: Context) {
        IMInit {
            context = app
            key = "mgb7ka1nmdndg"
            tokenProvider = IMTokenProviderImpl()
            fileUploadProvider = IMFileUploadProviderImpl()
        }
//    initSticker(App)
//    registerIMMessage()
//    registerIMMessageItem()
        addIMListener()
    }

    /**
     * IM是否登录了
     */
    fun isIMLogin(): Boolean {
        return isIMLogin
    }

    /**
     * 登录IM
     */
    fun login(success: (() -> Unit)? = null, failed: ((msg: String?) -> Unit)? = null) {
        IM.addActionListener(IMConstant.LoginAction.ACTION_IM_LOGIN_SUCCESS) {
            success?.invoke()
            isIMLogin = true
            IMLoginGlobal.setImLogin(true)
            LogUtils.e("IM 融云登录成功")
        }
        IM.addActionListener(IMConstant.LoginAction.ACTION_IM_LOGIN_ERROR) {
            LogUtils.e("IM 融云登录失败,msg:${IMActionEntity.getErrorMessage(it.data)}")
            isIMLogin = false
            IMLoginGlobal.setImLogin(false)
            failed?.invoke(IMActionEntity.getErrorMessage(it.data))
        }
        IM.login()
    }

    /**
     * 退出登录
     */
    fun logout() {
        IM.logout()
        isIMLogin = false
    }

    private fun addIMListener() {
        IM.addActionListener(IMConstant.ConnectionAction.ACTION_IM_CONNECTION_KICKED_OFFLINE_BY_OTHER_CLIENT) {
            //要增加清除数据和跳转到登录页面
//      UserRepository.clearUserInfo()
            logout()
            BaseApplication.getApp().toast("您的账号已在其他设备登录")
            ActivityUtils.finishAllActivities()
//      LoginComponent.startLogin()
            LogUtils.e("ConversationViewModel 用户在其他设备上登录，被挤下线")
        }
        IM.addMessageReceivedListener {
            LogUtils.e("RongIm 收到新消息 messge=${GsonUtils.toJson(it)}")
            //收到新消息
//      val imInfoRepository: IMInfoRepository by App.INSTANCE.kodein.instance()
//      ConversationUpdateGlobal.setConversationUpdate(it.conversationType, it.targetId)
//      when {
//        it.conversationType == Conversation.ConversationType.PRIVATE -> //私聊
//          it.content?.userInfo?.let { userInfo ->
//            imInfoRepository.setUserPrivateInfo(
//              userInfo.userId.toLong(),
//              userInfo.portraitUri.toString(),
//              userInfo.name
//            )
//          }
//        it.conversationType == Conversation.ConversationType.GROUP -> //群聊
//          it.content?.userInfo?.let { userInfo ->
//            val name = if (userInfo.extra.isNullOrBlank()) {
//              userInfo.name
//            } else {
//              val jsonObj = JSONObject(userInfo.extra)
//              if (jsonObj.has("aliasName")) {
//                jsonObj.optString("aliasName")
//              } else {
//                userInfo.name
//              }
//            }
//            imInfoRepository.setUserGroupInfo(
//              userInfo.userId.toLong(),
//              it.targetId.toLong(),
//              userInfo.portraitUri.toString(),
//              name
//            )
//          }
//        it.conversationType == Conversation.ConversationType.SYSTEM -> {
//          //系统消息
//          if (it.objectName == MessageTypeConstants.MESSAGE_TYPE_SYSTEM_CHAT) {
//            LogUtils.e("收到一条 聊天中的系统消息")
//            //聊天中的系统消息
//            //先更新为已读
//            val disposable = AimyIm.updateReceivedMessageRead(it.messageId).subscribe({}, {})
//            //然后插入一条到群聊中
//            val messageContent = it.content
//            if (messageContent is ChatSystemMessage) {
//              messageContent.getContentBean()?.groupId?.let { groupId ->
//                if (groupId.isNotBlank()) {
//                  LogUtils.e("向群聊id=${groupId}中插入一条 聊天中的系统消息")
//                  AimyIm.insertSendMessage(
//                    Conversation.ConversationType.GROUP,
//                    groupId,
//                    SystemMessage(messageContent.content)
//                  ).subscribe({ message ->
//                    LogUtils.e("向群聊id=${groupId}中插入一条 聊天中的系统消息 成功")
//                    LocalMessageAddGlobal.setNewLocalMessage(message)
//                  }, {
//                  })
//                }
//              }
//            }
//          }
//        }
//      }
        }
    }

//  //初始化表情包相关
//  private fun initSticker(context: Context) {
//    val stickerDirPath = AppFileDirManager.getImStickerDir(context)
//    FileUtils.createOrExistsDir(stickerDirPath)
//    ImUiManager
//      .init(context, stickerPath = stickerDirPath, imageLoader = object :
//        IImageLoader {
//        override fun displayImage(context: Context, path: String, imageView: ImageView) {
//          //加载图片
//          imageView.load(path)
//        }
//      })
//  }
//
//  /**
//   * 向融云注册自定义的消息类型
//   */
//  private fun registerIMMessage() {
//    AimyIm.registerMessageType(ShortVideoMessage::class.java)
//    AimyIm.registerMessageType(UserContactMessage::class.java)
//    AimyIm.registerMessageType(CircleContactMessage::class.java)
//    AimyIm.registerMessageType(FeedMessage::class.java)
//    AimyIm.registerMessageType(SystemMessage::class.java)
//    AimyIm.registerMessageType(ChatSystemMessage::class.java)
//  }
//
//  /**
//   * 注册Item
//   */
//  private fun registerIMMessageItem() {
//    //文字类型消息
//    MessageItemFactory.registerMessageItem(TextMessage::class, TextMessageItem_::class)
//    //图片类型的消息
//    MessageItemFactory.registerMessageItem(ImageMessage::class, ImageMessageItem_::class)
//    //语音类型的消息
//    MessageItemFactory.registerMessageItem(VoiceMessage::class, VoiceMessageItem_::class)
//    //视频类型的消息
//    MessageItemFactory.registerMessageItem(ShortVideoMessage::class, ShortVideoMessageItem_::class)
//    //用户名片的消息
//    MessageItemFactory.registerMessageItem(
//      UserContactMessage::class,
//      UserContactMessageItem_::class
//    )
//    //圈子名片的消息
//    MessageItemFactory.registerMessageItem(
//      CircleContactMessage::class,
//      CircleContactMessageItem_::class
//    )
//    //动态消息
//    MessageItemFactory.registerMessageItem(
//      FeedMessage::class,
//      FeedMessageItem_::class
//    )
//    //系统消息
//    MessageItemFactory.registerMessageItem(
//      SystemMessage::class,
//      SystemMessageItem_::class
//    )
//    //聊天中的系统消息
//    MessageItemFactory.registerMessageItem(
//      ChatSystemMessage::class,
//      ChatSystemMessageItem_::class
//    )
//  }
}