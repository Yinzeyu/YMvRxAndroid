package com.yzy.sociallib.utils

import android.content.Context
import com.blankj.utilcode.util.SDCardUtils

import java.io.File

/**
 * description :
 * @date 2019/7/15
 * @author: yzy.
 */
object FilePathUtils {
    /**
     * 公司文件夹名称
     */
    private const val COMPANY_FOLDER = "zhi_xiang"
    /**
     * 图片
     */
    const val IMAGES = "images"

    /**
     * 获取自定义的app的主目录
     */
    fun getAppPath(context: Context): String {
        val rootDir = if (SDCardUtils.getSDCardPathByEnvironment().isNotBlank())
            SDCardUtils.getSDCardPathByEnvironment()
        else
            context.cacheDir.path
        return (rootDir
                + File.separator
                + COMPANY_FOLDER
                + File.separator)
    }

    /**
     * 初始化项目的各种文件夹
     */
    fun initAppFile(context: Context) {
        val appPath = getAppPath(context)
        //图片
        FileUtils.createOrExistsDir(appPath + IMAGES)
    }

    fun getProvider(context: Context): String {
        return context.packageName + ".version3.provider"
    }

}
