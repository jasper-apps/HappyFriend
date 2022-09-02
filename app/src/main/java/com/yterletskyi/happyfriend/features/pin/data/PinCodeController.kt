package com.yterletskyi.happyfriend.features.pin.data

import android.content.SharedPreferences
import com.yterletskyi.happyfriend.common.LifecycleComponent

interface PinCodeController : LifecycleComponent {
    val pinCode: PinCode?
    fun savePinCode(pinCode: PinCode?)
}

class SharedPrefPinCodeController(
    private val sharedPreferences: SharedPreferences
) : PinCodeController {

    override var pinCode: PinCode = PinCode("")

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs: SharedPreferences, key: String ->
            if (key == KEY) {
                pinCode.pin = prefs.getString(key, "")!!
            }
        }

    override fun savePinCode(pinCode: PinCode?) {
        sharedPreferences.edit()
            .putString(KEY, pinCode?.pin)
            .apply()
    }

    override fun initialize() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun destroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    companion object {
        private const val KEY = "pin_code_key"
    }
}
