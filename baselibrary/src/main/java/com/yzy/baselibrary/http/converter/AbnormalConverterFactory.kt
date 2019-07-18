package com.yzy.baselibrary.http.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 *description: 畸形转换.
 *@date 2019/7/15
 *@author: yzy.
 */
class AbnormalConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

  companion object {
    fun create(): AbnormalConverterFactory {
      return AbnormalConverterFactory(Gson())
    }
  }

  override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                     retrofit: Retrofit): Converter<ResponseBody, *> {
    val adapter = gson.getAdapter(TypeToken.get(type))
    return AbnormalResponseBodyConverter(gson, adapter)
  }

  override fun requestBodyConverter(type: Type,
                                    parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody> {
    val adapter = gson.getAdapter(TypeToken.get(type))
    return AbnormalRequestBodyConverter(gson, adapter)
  }
}