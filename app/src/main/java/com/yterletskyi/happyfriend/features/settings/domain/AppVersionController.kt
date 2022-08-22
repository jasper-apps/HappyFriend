package com.yterletskyi.happyfriend.features.settings.domain

interface AppVersionController {
    fun getAppVersion(): String
}

class MockAppVersionController : AppVersionController {
    override fun getAppVersion(): String = "mock-v0.0.1"
}

class RealAppVersionController : AppVersionController {
    override fun getAppVersion(): String = TODO()
}
