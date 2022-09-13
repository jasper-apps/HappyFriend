package com.yterletskyi.happyfriend.common.analytics

class CompoundAnalytics(
    private val analytics1: Analytics,
    private val analytics2: Analytics,
) : Analytics {

    override fun setUserProperty(property: Analytics.UserProperty<*>) {
        analytics1.setUserProperty(property)
        analytics2.setUserProperty(property)
    }
}