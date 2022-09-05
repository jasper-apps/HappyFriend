package com.yterletskyi.happyfriend.common.logger

import android.util.Log
import com.yterletskyi.happyfriend.BuildConfig

interface Logger {
    fun info(message: String)
    fun error(exception: Exception)
}

class LogcatLogger(
    private val tag: String,
    private val enable: Boolean = BuildConfig.DEBUG,
) : Logger {

    override fun info(message: String) {
        if (enable) {
            Log.i(tag, message)
        }
    }

    override fun error(exception: Exception) {
        if (enable) {
            Log.e(tag, exception.message, exception)
        }
    }
}
