package com.yterletskyi.happyfriend.features.settings.domain

import com.yterletskyi.happyfriend.BuildConfig

interface AppVersionController {
    fun getAppVersion(): String
}

class MockAppVersionController : AppVersionController {
    override fun getAppVersion(): String = "mock-v0.0.1"
}

class RealAppVersionController : AppVersionController {
    override fun getAppVersion(): String = BuildConfig.VERSION_NAME
}
