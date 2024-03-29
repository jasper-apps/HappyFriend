package com.jasperapps.happyfriend.features.pin.data

import android.content.SharedPreferences
import com.jasperapps.happyfriend.features.pin.domain.PinCode

interface PinCodeController {
    fun getPinCode(): PinCode?
    fun savePinCode(pinCode: PinCode?)
}

class SharedPrefPinCodeController(
    private val sharedPreferences: SharedPreferences
) : PinCodeController {

    override fun getPinCode(): PinCode? {
        val pin = sharedPreferences.getString(KEY, null)
        return pin?.let { PinCode(it) }
    }

    override fun savePinCode(pinCode: PinCode?) {
        sharedPreferences.edit()
            .putString(KEY, pinCode?.toString())
            .apply()
    }

    companion object {
        private const val KEY = "pin_code_key"
    }
}
