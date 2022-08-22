package com.yterletskyi.happyfriend.features.settings.domain

import android.content.SharedPreferences
import com.yterletskyi.happyfriend.common.LifecycleComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface MyWishlistController : LifecycleComponent {
    val wishlistFlow: Flow<Boolean>
}

interface InternalMyWishlistController : MyWishlistController {
    fun setMyWishListEnabled(enabled: Boolean)
}

class SharedPrefsMyWishlistController(
    private val sharedPreferences: SharedPreferences,
) : InternalMyWishlistController {

    private val _wishlistFlow: MutableStateFlow<Boolean> = MutableStateFlow(
        sharedPreferences.getBoolean(KEY, false)
    )

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs: SharedPreferences, key: String ->
            if (key == KEY) {
                _wishlistFlow.value = prefs.getBoolean(KEY, false)
            }
        }

    override val wishlistFlow: Flow<Boolean> = _wishlistFlow

    override fun initialize() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun setMyWishListEnabled(enabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(KEY, enabled)
            .apply()
    }

    override fun destroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val KEY = "my_wishlist_enabled_key"
    }
}
