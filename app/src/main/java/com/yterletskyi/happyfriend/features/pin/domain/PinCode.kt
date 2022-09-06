package com.yterletskyi.happyfriend.features.pin.domain

import android.util.Log
import java.util.EmptyStackException
import java.util.Enumeration
import java.util.Stack

class LoggedPinCode(
    private val actualPinCode: PinCode
) : Stack<String>() {

    override fun push(item: String): String {
        val pushed = actualPinCode.push(item)
        Log.i("info23", "$pushed pushed. Value: ${elements().toList()}")
        return pushed
    }

    override fun pop(): String {
        val popped = actualPinCode.pop()
        Log.i("info23", "$popped popped. Value: ${elements().toList()}")
        return popped
    }

    override fun elements(): Enumeration<String> {
        return actualPinCode.elements()
    }

    override val size: Int
        get() = actualPinCode.size
}

class PinCode(private val maxLength: Int) : Stack<String>() {

    constructor(pin: String) : this(pin.length) {
        pin.forEach {
            push(it.toString())
        }
    }

    fun getPinCode(): String {
        val pinCode: StringBuilder = StringBuilder()
        super.elements().toList().forEach {
            pinCode.append(it)
        }
        return pinCode.toString()
    }

    override fun push(item: String): String {
        if (elementCount < maxLength) {
            return super.push(item)
        } else throw PinSizeExceededException("PinCode max length exceeded")
    }

    override fun pop(): String {
        try {
            return super.pop()
        } catch (e: EmptyStackException) {
            throw PinSizeExceededException("PinCode is already empty")
        }
    }
}
