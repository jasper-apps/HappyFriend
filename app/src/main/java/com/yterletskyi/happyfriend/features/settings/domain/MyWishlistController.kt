package com.yterletskyi.happyfriend.features.settings.domain

import android.content.SharedPreferences

interface MyWishlistController {
    fun isMyWishlistEnabled(): Boolean
}

interface InternalMyWishlistController : MyWishlistController {
    fun setMyWishListEnabled(enabled: Boolean)
}

class AlwaysEnabledMyWishlistController : MyWishlistController {
    override fun isMyWishlistEnabled(): Boolean = true
}

class SharedPrefsMyWishlistController(
    private val sharedPreferences: SharedPreferences,
) : InternalMyWishlistController {

    override fun isMyWishlistEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY, false)
    }

    override fun setMyWishListEnabled(enabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(KEY, enabled)
            .apply()
    }

    companion object {
        private const val KEY = "my_wishlist_enabled_key"
    }
}