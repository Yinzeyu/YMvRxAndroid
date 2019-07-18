package com.aimyfun.android.sociallibrary.utils

import android.content.Context
import android.os.storage.StorageManager
import java.lang.reflect.InvocationTargetException
import java.util.ArrayList
import java.util.Arrays

/**
 * description :
 *
 * @author : yinzeyu
 * @date : 2018/8/25 15:13
 */
class SDCardUtils private constructor() {
    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        /**
         * Return whether sdcard is enabled.
         *
         * @return true : enabled<br></br>false : disabled
         */
        val isSDCardEnable: Boolean
            get() = !sdCardPaths.isEmpty()

        /**
         * Return the paths of sdcard.
         *
         * @return the paths of sdcard
         */
        val sdCardPaths: List<String>
            get() {
                val storageManager = Utils.getApp()
                    .getSystemService(Context.STORAGE_SERVICE) as StorageManager
                var paths: MutableList<String> = mutableListOf()
                try {
                    val getVolumePathsMethod =
                        StorageManager::class.java.getMethod("getVolumePaths")
                    getVolumePathsMethod.setAccessible(true)
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

                return paths
            }
    }
}
