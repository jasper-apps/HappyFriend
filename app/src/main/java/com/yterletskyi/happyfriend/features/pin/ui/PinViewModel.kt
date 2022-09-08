package com.yterletskyi.happyfriend.features.pin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
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
    handle: SavedStateHandle
) : ViewModel() {

    val pinMaxLengthLiveData: LiveData<Int> = MutableLiveData(PIN_CODE_MAX_LENGTH)

    private val _pinProgressLiveData: MutableLiveData<Int> = MutableLiveData(0)
    val pinProgressLiveData: LiveData<Int> = _pinProgressLiveData

    val titleLiveData: LiveData<Int> = liveData {
        val title =
            if (isRepeatPinMode) R.string.pin_repeat_title
            else R.string.pin_enter_title
        emit(title)
    }

    val errorLiveData: MutableLiveData<Int> = MutableLiveData()
    val directionsData: MutableLiveData<NavDirections> = MutableLiveData()

    private val isRepeatPinMode: Boolean = handle["isRepeatPinMode"]
        ?: error("isRepeatPinMode is not passed")

    private val previousPin: PinCode? = handle.get<String>("pin")
        ?.let { PinCode(it) }

    private val currentPin: PinCode = PinCode(PIN_CODE_MAX_LENGTH)

    fun input(what: PinButtonModel) {
        try {
            when (what) {
                is PinButtonModel.Number -> currentPin.push(what.value.toString())
                PinButtonModel.Erase -> currentPin.pop()
            }
            _pinProgressLiveData.value = currentPin.size
        } catch (e: PinSizeExceededException) {
            // just ignore
        }
        if (currentPin.size == PIN_CODE_MAX_LENGTH) {
            authorize()
        }
    }

    private fun authorize() {
        val usersPin = pinCodeController.getPinCode()
        if (usersPin == null) {  // creating new pin
            if (isRepeatPinMode) {
                // verify
                val pinsMatch = previousPin == currentPin
                // show friends
                if (pinsMatch) {
                    pinCodeController.savePinCode(currentPin)
                    directionsData.value = PinFragmentDirections.toFriendScreen()
                } else {
                    showError()
                }
            } else {
                // show repeat pin
                directionsData.value = PinFragmentDirections.toPinScreen(
                    isRepeatPinMode = true,
                    pin = currentPin.toString()
                )
            }
        } else {  // authorizing with existing pin
            // verify
            val pinsMatch = usersPin == currentPin
            // show friends
            if (pinsMatch) {
                directionsData.value = PinFragmentDirections.toFriendScreen()
            } else {
                showError()
            }
        }
    }

    private fun showError() {
        errorLiveData.value = R.string.pin_enter_error
        currentPin.clear()
    }

    companion object {
        const val PIN_CODE_MAX_LENGTH = 4
    }
}
