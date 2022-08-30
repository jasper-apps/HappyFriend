package com.yterletskyi.happyfriend.common.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class PinKeyboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    init {
    }

    private class PinKeyboardButtonView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
    ) : ConstraintLayout(context, attrs) {

    }
}