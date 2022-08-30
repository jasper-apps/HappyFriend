package com.yterletskyi.happyfriend.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yterletskyi.happyfriend.databinding.ViewPinKeyboardBinding

class PinKeyboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: ViewPinKeyboardBinding? = null
    private val binding: ViewPinKeyboardBinding get() = _binding!!

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewPinKeyboardBinding.inflate(inflater, this)
    }
}