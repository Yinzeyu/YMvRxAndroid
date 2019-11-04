package com.yzy.baselibrary.http.converter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.yzy.baselibrary.BuildConfig
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter

/**
 *description: 返回值data为空或者data直接为常量的转换.
 *@date 2019/7/15
 *@author: yzy.
 */
class AbnormalResponseBodyConverter<T> constructor(
        private val gson: Gson,
        private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {
    private val KEY_DATA = "data"
    private val KEY_CODE = "code"
    private val DATA_LIST_COLVER = "{\"data\":[]}"
    private val EMPTY_DATA_OBJECT_CONVER = "{\"data\":{}}"
    private val EMPTY_DATA_LIST_ADD = "{\"data\":[],"
    private val EMPTY_DATA_OBJECT_ADD = "{\"data\":{},"
    override fun convert(value: ResponseBody): T {
        if (value.contentLength() > Int.MAX_VALUE) {
            //超出String字符串的长度
            value.use {
                val jsonReader = gson.newJsonReader(it.charStream())
                return adapter.read(jsonReader)
            }
        }

        var resStr = String(value.bytes())

        val resJsonOb = JSONObject(resStr)
        /**
         * 增加这个判断是防止有data 但是为null 的情况
         */
        if (resJsonOb.has(KEY_DATA) && resJsonOb.getString(KEY_DATA) == "null") {
            resJsonOb.remove(KEY_DATA)
            resStr = resJsonOb.toString()
        }
        if (resStr.isNotEmpty()
                && resStr.startsWith("{")
                && resJsonOb.has(KEY_CODE)
                && resJsonOb.getInt(KEY_CODE) == 200
                && !resJsonOb.has(KEY_DATA)
        ) {
            try {
                adapter.fromJson(EMPTY_DATA_OBJECT_CONVER)
                resStr = EMPTY_DATA_OBJECT_ADD + resStr.substring(1)
            } catch (e: Exception) {
                try {
                    adapter.fromJson(DATA_LIST_COLVER)
                    resStr = EMPTY_DATA_LIST_ADD + resStr.substring(1)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("..BodyConverter", "data is not list and object")
                }
            }
        }
        if (BuildConfig.DEBUG && !resStr.isEmpty()) {
            //默认debug打印不出来
//      LogUtils.json(LogUtils.I, "服务器返回数据", resStr)
        }
        return adapter.fromJson(resStr)
    }
}