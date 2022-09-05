package com.yterletskyi.happyfriend.common.logger

inline fun <reified C : Any> C.loggers(
    noinline tag: () -> String? = { C::class.simpleName }
): Lazy<Logger> {
    return lazy {
        LogcatLogger(
            tag = tag()
                ?: throw IllegalStateException(
                    "Logging in anonymous classes require non-null tag parameter"
                )
        )
    }
}
