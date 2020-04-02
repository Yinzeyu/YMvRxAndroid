package com.yzy.baselibrary.extention

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
        Internals.internalStartActivity(this, T::class.java, params)

inline fun <reified T : Activity> Activity.startActivityForResult(
        requestCode: Int,
        vararg params: Pair<String, Any?>
) =
        Internals.internalStartActivityForResult(this, T::class.java, requestCode, params)


inline fun <reified T : Service> Context.startService(vararg params: Pair<String, Any?>) =
        Internals.internalStartService(this, T::class.java, params)


inline fun <reified T : Service> Context.stopService(vararg params: Pair<String, Any?>) =
        Internals.internalStopService(this, T::class.java, params)


inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
        Internals.createIntent(this, T::class.java, params)


fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

fun Intent.newDocument(): Intent = apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
}

fun Intent.excludeFromRecents(): Intent =
        apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }


fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }


object Internals {
    @JvmStatic
    fun <T> createIntent(
            ctx: Context,
            clazz: Class<out T>,
            params: Array<out Pair<String, Any?>>
    ): Intent {
        val intent = Intent(ctx, clazz)
        if (params.isNotEmpty()) fillIntentArguments(intent, params)
        return intent
    }

    @JvmStatic
    fun internalStartActivity(
            ctx: Context,
            activity: Class<out Activity>,
            params: Array<out Pair<String, Any?>>
    ) {
        ctx.startActivity(createIntent(ctx, activity, params))
    }

    @JvmStatic
    fun internalStartActivityForResult(
            act: Activity,
            activity: Class<out Activity>,
            requestCode: Int,
            params: Array<out Pair<String, Any?>>
    ) {
        act.startActivityForResult(createIntent(act, activity, params), requestCode)
    }

    @JvmStatic
    fun internalStartService(
            ctx: Context,
            service: Class<out Service>,
            params: Array<out Pair<String, Any?>>
    ): ComponentName? = ctx.startService(createIntent(ctx, service, params))

    @JvmStatic
    fun internalStopService(
            ctx: Context,
            service: Class<out Service>,
            params: Array<out Pair<String, Any?>>
    ): Boolean = ctx.stopService(createIntent(ctx, service, params))

    @JvmStatic
    private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            when (val value = it.second) {
                null -> intent.putExtra(it.first, null as Serializable?)
                is Int -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Char -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is Serializable -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                    else -> throw YException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(it.first, value)
                is LongArray -> intent.putExtra(it.first, value)
                is FloatArray -> intent.putExtra(it.first, value)
                is DoubleArray -> intent.putExtra(it.first, value)
                is CharArray -> intent.putExtra(it.first, value)
                is ShortArray -> intent.putExtra(it.first, value)
                is BooleanArray -> intent.putExtra(it.first, value)
                else -> throw YException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            return@forEach
        }
    }

}

open class YException(message: String = "") : RuntimeException(message)