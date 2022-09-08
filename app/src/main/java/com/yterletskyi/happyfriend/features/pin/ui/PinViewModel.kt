package com.yterletskyi.happyfriend.features.pin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.features.pin.data.PinCodeController
import com.yterletskyi.happyfriend.features.pin.domain.PinButtonModel
import com.yterletskyi.happyfriend.features.pin.domain.PinCode
import com.yterletskyi.happyfriend.features.pin.domain.PinSizeExceededException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val pinCodeController: PinCodeController,
    private val handle: SavedStateHandle
) : ViewModel() {

    val pinMaxLengthLiveData: LiveData<Int> = MutableLiveData(PIN_CODE_MAX_LENGTH)

    private val _pinProgressLiveData: MutableLiveData<Int> = MutableLiveData(0)
    val pinProgressLiveData: LiveData<Int> = _pinProgressLiveData
    val errorLiveData: MutableLiveData<Int> = MutableLiveData()
    val directionsData: MutableLiveData<NavDirections> = MutableLiveData()

    private var tempPin: String? = handle["pin"]
        ?: error("pin is not passed")

    private val pin: PinCode = PinCode(PIN_CODE_MAX_LENGTH)

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
        if (pin.size == PIN_CODE_MAX_LENGTH) {
            authorize()
        }
    }

    private fun authorize() {
        if (tempPin == "") {
            directionsData.value = PinFragmentDirections.toPinScreen(R.string.pin_repeat_title, pin.toString())
        } else {
            if (pinCodeController.getPinCode() == null) {
                when (tempPin) {
                    pin.toString() -> {
                        pinCodeController.savePinCode(pin)
                        tempPin = null
                        directionsData.value = PinFragmentDirections.toFriendScreen()
                    }
                    else -> {
                        errorLiveData.value = R.string.pin_enter_error
                        pin.clear()
                    }
                }
            } else {
                when (pinCodeController.getPinCode()?.toString()) {
                    pin.toString() -> {
                        directionsData.value = PinFragmentDirections.toFriendScreen()
                    }
                    else -> {
                        errorLiveData.value = R.string.pin_enter_error
                        pin.clear()
                    }
                }
            }
        }
    }

    companion object {
        const val PIN_CODE_MAX_LENGTH = 4
    }
}
