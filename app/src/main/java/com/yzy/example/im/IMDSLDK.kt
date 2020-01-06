//package com.yzy.example.im
//
//import android.content.Context
//import android.widget.ImageView
//import com.blankj.utilcode.util.FileUtils
//import com.yzy.example.extention.load
//import com.yzy.example.im.entity.SendMessageBean
//import com.yzy.example.im.entity.SendMessageConfig
//import com.yzy.example.imModel.emoji.IImageLoader
//import com.yzy.example.imModel.emoji.ImUiManager
//import com.yzy.example.widget.file.AppFileDirManager
//
///**
// *description: IM相关的DSL.
// *@date 2019/3/12 11:09.
// *@author: yzy.
// */
//
///**
// * IM初始化的DSL
// */
//fun IMInit(config: IMInitConfig.() -> Unit) {
//    val configBean = IMInitConfig()
//    configBean.apply(config)
//    IM.init(configBean)
//}
//
///**
// * 发送im消息的DSL
// */
//fun IMSend(config: SendMessageConfig.() -> Unit) {
//    val configBean = SendMessageConfig()
//    configBean.apply(config)
//    val bean = SendMessageBean(
//        configBean.conversationType,
//        configBean.targetId,
//        configBean.content,
//        configBean.pushContent,
//        configBean.pushData,
//        configBean.messageType,
//        configBean.success,
//        configBean.error,
//        configBean.attached,
//        configBean.canceled,
//        configBean.progress
//    )
//    IM.sendMessage(bean)
//}
//
////初始化表情包相关
//fun initSticker(context: Context) {
//    val stickerDirPath = AppFileDirManager.getImStickerDir(context)
//    FileUtils.createOrExistsDir(stickerDirPath)
//    ImUiManager
//        .init(context, stickerPath = stickerDirPath, imageLoader = object :
//            IImageLoader {
//            override fun displayImage(context: Context, path: String, imageView: ImageView) {
//                //加载图片
//                imageView.load(path)
//            }
//        })
//}