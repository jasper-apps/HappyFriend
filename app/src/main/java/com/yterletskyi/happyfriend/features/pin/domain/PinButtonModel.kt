package com.yterletskyi.happyfriend.features.pin.domain

sealed interface PinButtonModel {
    data class Number(val value: Int) : PinButtonModel
    object Erase : PinButtonModel
}