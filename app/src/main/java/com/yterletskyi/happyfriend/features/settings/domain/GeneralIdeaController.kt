package com.yterletskyi.happyfriend.features.settings.domain

import android.content.SharedPreferences
import com.yterletskyi.happyfriend.common.LifecycleComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface GeneralIdeaController : LifecycleComponent {
    val generalidealist : Flow<Boolean>
}

interface InternalGeneralIdeaController : GeneralIdeaController {
    fun setListEnabled(enabled: Boolean)
}

class SharedPrefGeneralIdeaController(private val sharedPreferences: SharedPreferences
) : InternalGeneralIdeaController {

    val _generalIdeasFlow: MutableStateFlow<Boolean> = MutableStateFlow(
    sharedPreferences.getBoolean(KEY, false)
    )

    override val generalidealist: Flow<Boolean> = _generalIdeasFlow

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs: SharedPreferences, key: String ->
            if (key == KEY) {
                _generalIdeasFlow.value = prefs.getBoolean(KEY, false)
            }
        }

    override fun initialize() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun setListEnabled(enabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(KEY, enabled)
            .apply()
    }

    override fun destroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val KEY = "my_generalidea_enabled_key"
    }

}