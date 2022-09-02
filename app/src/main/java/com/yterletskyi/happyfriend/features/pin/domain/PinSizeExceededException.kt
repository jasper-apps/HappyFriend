package com.yterletskyi.happyfriend.features.pin.domain

class PinSizeExceededException(override val message: String) : IllegalStateException(message)