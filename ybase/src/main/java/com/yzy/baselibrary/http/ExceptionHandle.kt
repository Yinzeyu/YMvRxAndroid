package com.yzy.baselibrary.http

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 *   @auther : Aleyn
 *   time   : 2019/08/12
 */
object ExceptionHandle {

    fun handleException(e: Throwable): ResponseThrowable {
        val ex: ResponseThrowable
        if (e is HttpException) {
            ex = ResponseThrowable(ERROR.HTTP_ERROR, e)
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException || e is MalformedJsonException
        ) {
            ex = ResponseThrowable(ERROR.PARSE_ERROR, e)
        } else if (e is ConnectException) {
            ex = ResponseThrowable(ERROR.NETWORD_ERROR, e)
        } else if (e is javax.net.ssl.SSLException) {
            ex = ResponseThrowable(ERROR.SSL_ERROR, e)
        } else if (e is java.net.SocketTimeoutException) {
            ex = ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.UnknownHostException) {
            ex = ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
        } else {
               ex = when(e){
                is ResponseThrowable -> {
                    val message = e.message ?: e.errMsg
                    ResponseThrowable(e.code, message)
                }
                else -> {
                    ResponseThrowable(ERROR.UNKNOWN, e)
                }
            }
//            var es = if ( e is ResponseThrowable) e else ResponseThrowable(1000,e.message?:"")
//            ex = if (!e.message.isNullOrEmpty()) ResponseThrowable(es.code, e.message?:"")
//            else ResponseThrowable(ERROR.UNKNOWN, e)
        }
        return ex
    }
}