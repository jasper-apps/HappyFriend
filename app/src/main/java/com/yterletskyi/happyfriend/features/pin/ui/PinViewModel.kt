package com.yterletskyi.happyfriend.features.pin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yterletskyi.happyfriend.features.pin.domain.LoggedPinCode
import com.yterletskyi.happyfriend.features.pin.domain.PinButtonModel
import com.yterletskyi.happyfriend.features.pin.domain.PinCode
import com.yterletskyi.happyfriend.features.pin.domain.PinSizeExceededException
import java.util.*

class PinViewModel : ViewModel() {

    val pinMaxLengthLiveData: LiveData<Int> = MutableLiveData(PIN_CODE_MAX_LENGTH)

    private val _pinProgressLiveData: MutableLiveData<Int> = MutableLiveData(0)
    val pinProgressLiveData: LiveData<Int> = _pinProgressLiveData

    private val pin: Stack<String> = LoggedPinCode(PinCode(PIN_CODE_MAX_LENGTH))

    fun input(what: PinButtonModel) {
        try {
            when (what) {
                is PinButtonModel.Number -> pin.push(what.value.toString())
                PinButtonModel.Erase -> pin.pop()
            }
            _pinProgressLiveData.value = pin.size
        } catch (e: PinSizeExceededException) {
            // just ignore
        }
    }

    companion object {
        const val PIN_CODE_MAX_LENGTH = 4
    }
}