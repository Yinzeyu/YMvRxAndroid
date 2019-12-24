package com.yzy.baselibrary.http.converter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
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
    override fun convert(value: ResponseBody): T {
        if (value.contentLength() > Int.MAX_VALUE) {
            //超出String字符串的长度
            value.use {
                val jsonReader = gson.newJsonReader(it.charStream())
                return adapter.read(jsonReader)
            }
        }
        val resStr = String(value.bytes())
        if (resStr.isNotEmpty()) {
            Log.e("response_star","┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━响应结果拦截开始━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━>>>")
            //默认debug打印不出来
            e("response",resStr)
            Log.e("response_end","┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━响应结果拦截结束━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━<<<")
        }
        return adapter.fromJson(resStr)
    }

    //规定每段显示的长度
    private val MAXLENGTH = 2000

    fun e(TAG: String, msg: String) {
        val strLength = msg.length
        var start = 0
        var end = MAXLENGTH
        for (i in 0..99) { //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end))
                start = end
                end += MAXLENGTH
            } else {
                Log.e(TAG, msg.substring(start, strLength))
                break
            }
        }
    }
}