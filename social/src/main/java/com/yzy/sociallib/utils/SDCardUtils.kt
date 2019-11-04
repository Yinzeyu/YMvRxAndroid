package com.yzy.sociallib.utils

import android.content.Context
import android.os.storage.StorageManager
import android.util.Log
import java.lang.reflect.InvocationTargetException

/**
 * description :
 * @date 2019/7/15
 * @author: yzy.
 */
object SDCardUtils {
    lateinit var paths: List<String>

    fun initSDCardPaths(context: Context): List<String> {
        Log.d("socially1", "init sd path")
        val storageManager = context
                .getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val paths: MutableList<String> = mutableListOf()
        try {
            val getVolumePathsMethod = StorageManager::class.java.getMethod("getVolumePaths")
            getVolumePathsMethod.isAccessible = true
            val invoke: MutableList<*> =
                    getVolumePathsMethod.invoke(storageManager) as MutableList<*>

            for (any in invoke) {
                paths.add(any as String)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        SDCardUtils.paths = paths
        return paths
    }
}
