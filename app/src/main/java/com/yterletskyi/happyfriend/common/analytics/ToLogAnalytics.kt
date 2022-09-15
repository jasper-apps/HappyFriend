package com.yterletskyi.happyfriend.common.analytics

import com.yterletskyi.happyfriend.common.logger.logcatLogger

class ToLogAnalytics : Analytics {

    private val logger by logcatLogger { "analytics" }

    override fun setUserProperty(property: Analytics.UserProperty<*>) {
        logger.info("user property set: <${property.key}>: <${property.value}>")
    }
}
