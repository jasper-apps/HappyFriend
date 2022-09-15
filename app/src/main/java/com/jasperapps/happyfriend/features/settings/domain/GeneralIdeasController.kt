package com.jasperapps.happyfriend.features.settings.domain

import android.content.SharedPreferences
import com.jasperapps.happyfriend.common.LifecycleComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface GeneralIdeasController : LifecycleComponent {
    val generalIdeasFlow: Flow<Boolean>
}

interface InternalGeneralIdeasController : GeneralIdeasController {
    fun setGeneralIdeasEnabled(enabled: Boolean)
}

class SharedPrefGeneralIdeasController(
    private val sharedPreferences: SharedPreferences
) : InternalGeneralIdeasController {

    private val _generalIdeasFlow: MutableStateFlow<Boolean> = MutableStateFlow(
        sharedPreferences.getBoolean(KEY, false)
    )

    override val generalIdeasFlow: Flow<Boolean> = _generalIdeasFlow

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs: SharedPreferences, key: String ->
            if (key == KEY) {
                _generalIdeasFlow.value = prefs.getBoolean(KEY, false)
            }
        }

    override fun initialize() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun setGeneralIdeasEnabled(enabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(KEY, enabled)
            .apply()
    }

    override fun destroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val KEY = "general_ideas_enabled_key"
    }
}
