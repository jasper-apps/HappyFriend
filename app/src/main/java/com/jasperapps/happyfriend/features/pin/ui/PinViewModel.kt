package com.jasperapps.happyfriend.features.pin.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.navigation.NavDirections
import com.jasperapps.happyfriend.R
import com.jasperapps.happyfriend.common.SingleLiveEvent
import com.jasperapps.happyfriend.features.pin.data.PinCodeController
import com.jasperapps.happyfriend.features.pin.domain.PinButtonModel
import com.jasperapps.happyfriend.features.pin.domain.PinCode
import com.jasperapps.happyfriend.features.pin.domain.PinSizeExceededException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val context: Application,
    private val pinCodeController: PinCodeController,
    handle: SavedStateHandle
) : AndroidViewModel(context) {

    val pinMaxLengthLiveData: LiveData<Int> = MutableLiveData(PIN_CODE_MAX_LENGTH)

    private val _pinProgressLiveData: MutableLiveData<Int> = MutableLiveData(0)
    val pinProgressLiveData: LiveData<Int> = _pinProgressLiveData

    val titleLiveData: LiveData<String> = liveData {
        emit(title)
    }

    val errorLiveData: MutableLiveData<Int> = MutableLiveData()
    val directionsLiveData: SingleLiveEvent<NavDirections> =
        SingleLiveEvent()

    private val isChangingPin: Boolean = handle["isChangingPin"]
        ?: error("isChangingPin is not passed")

    private val title: String = handle["title"]
        ?: error("title is not passed")

    private val previousPin: PinCode? = handle.get<String?>("pin")
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

    /**
     * Next cases should be covered:
     * 1. no pin code -> create new
     * 2. user has a pin code -> validate it
     * 3. user wants to change the pin -> create new
     */
    private fun authorize() {
        val existingPin = pinCodeController.getPinCode()
        val hasPin = existingPin != null

        if (hasPin) {
            if (isChangingPin) {
                if (previousPin == null) {
                    directionsLiveData.value = PinFragmentDirections.toPinScreen(
                        title = context.getString(R.string.pin_repeat_title),
                        isChangingPin = true,
                        pin = currentPin.toString(),
                    )
                } else {
                    if (previousPin == currentPin) {
                        pinCodeController.savePinCode(currentPin)
                        directionsLiveData.value = PinFragmentDirections.popToSettingsScreen()
                    } else {
                        showError()
                    }
                }
            } else {
                if (existingPin == currentPin) {
                    directionsLiveData.value = PinFragmentDirections.toFriendsScreen()
                } else {
                    showError()
                }
            }
        } else {
            val isRepeatPinMode = previousPin != null
            if (isRepeatPinMode) {
                if (previousPin == currentPin) {
                    pinCodeController.savePinCode(currentPin)
                    directionsLiveData.value = PinFragmentDirections.toFriendsScreen()
                } else {
                    showError()
                }
            } else {
                directionsLiveData.value = PinFragmentDirections.toPinScreen(
                    title = context.getString(R.string.pin_repeat_title),
                    pin = currentPin.toString(),
                )
                _pinProgressLiveData.value = 0
                currentPin.clear()
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
