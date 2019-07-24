package com.yzy.pj.push


/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/12/26 15:54
 */
class OpenActivityManager {
//  companion object {
//    fun initPush(context: Context, msg: String) {
//      if (GameGlobal.isGaming()) {
//        GameModule.restartCCGameActivity(context)
//      } else {
//        val urlMap = AimyUriUtils.getUrlMap(msg)
//        if (urlMap != null) {
//          val title = urlMap[BuildUriConstants.URL_TITLE_MSG]
//          title?.let {
//            if (it == BuildUriConstants.CHATPAGE) {
//              val userId = urlMap[BuildUriConstants.URL_USER_ID]
//              if (userId != null) {
//                MessageModule.startMessageChatActivity(context, userId.toLong(), StatisticsLogConstant.ChatFrom.PushMessage)
//              }
//            } else if (it == BuildUriConstants.MY_MESSAGE_PAGE) {
//              UriStartActivityGlobal.setValue(it)
//            } else if (it == BuildUriConstants.GAME_MATCHING) {
//              UriStartActivityGlobal.setValue(it)
//            } else if (it == BuildUriConstants.TOPIC_PAGE) {
//              val userTopicId = urlMap[BuildUriConstants.URL_TOPIC_ID]
//              if (userTopicId != null) {
//                SquareModule.startTopicDetailActivity(context, userTopicId.toLong())
//              }
//            } else if (it == BuildUriConstants.SIGNIN_PAGE) {
//              //跳转到签到页面
//              UserModule.startSigninActivity(context)
//            } else if (it.startsWith("http") || it.startsWith("www")) {
//              MainModule.startWebActivity(context, null, it, true)
//            }
//          }
//        }
//      }
//    }
//
//    fun startMainActivity(context: Context) {
//      val intent = Intent(context, MainActivity::class.java)
//      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//      intent.addCategory(Intent.CATEGORY_HOME)
//      context.startActivity(intent)
//    }
//  }
}