package com.yterletskyi.happyfriend.features.pin.data

import android.content.SharedPreferences

interface PinCodeController {
    fun getPinCode(): PinCode?
    fun savePinCode(pinCode: PinCode?)
}

class SharedPrefPinCodeController(
    private val sharedPreferences: SharedPreferences
) : PinCodeController {

    override fun getPinCode(): PinCode? {
        // val pin = sharedPreferences.getString(KEY, null)
        // return pin?.let { PinCode(it) }
        return PinCode("1234")
    }

    override fun savePinCode(pinCode: PinCode?) {
        sharedPreferences.edit()
            .putString(KEY, pinCode?.pin)
            .apply()
    }

    companion object {
        private const val KEY = "pin_code_key"
    }
}
