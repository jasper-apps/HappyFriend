package com.yterletskyi.happyfriend.features.settings.domain

interface AppVersionController {
    fun getAppVersion(): String
}

class RealAppVersionController : AppVersionController {
    override fun getAppVersion(): String = TODO()
}