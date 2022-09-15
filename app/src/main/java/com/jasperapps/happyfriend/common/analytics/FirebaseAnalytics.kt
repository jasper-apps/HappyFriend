package com.jasperapps.happyfriend.common.analytics

import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalytics(
    private val firebaseAnalytics: FirebaseAnalytics,
) : Analytics {

    override fun setUserProperty(property: Analytics.UserProperty<*>) {
        firebaseAnalytics.setUserProperty(property.key, property.value.toString())
    }
}
