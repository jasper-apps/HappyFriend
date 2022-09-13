package com.jasperapps.happyfriend.features.pin.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.jasperapps.happyfriend.databinding.ViewPinKeyboardBinding
import com.jasperapps.happyfriend.features.pin.domain.PinButtonModel

class PinKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: ViewPinKeyboardBinding? = null
    private val binding: ViewPinKeyboardBinding get() = _binding!!

    var onButtonClicked: ((PinButtonModel) -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewPinKeyboardBinding.inflate(inflater, this)
    }

    init {
        with(binding) {
            btn1.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(1)) }
            btn2.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(2)) }
            btn3.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(3)) }
            btn4.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(4)) }
            btn5.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(5)) }
            btn6.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(6)) }
            btn7.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(7)) }
            btn8.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(8)) }
            btn9.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(9)) }
            btn0.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Number(0)) }
            btnErase.setOnClickListener { onButtonClicked?.invoke(PinButtonModel.Erase) }
        }
    }
}
