package com.yterletskyi.happyfriend.common.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.x.sp
import com.yterletskyi.happyfriend.databinding.ViewPinKeyboardButtonBinding

class PinKeyboardButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: ViewPinKeyboardButtonBinding? = null
    private val binding: ViewPinKeyboardButtonBinding get() = _binding!!

    @ColorInt
    var btnBackgroundColor: Int = Color.BLACK
        set(value) {
            field = value
            val drawable = PaintDrawable(value)
                .also { it.setCornerRadius(Float.MAX_VALUE) }
            background = drawable
        }

    var text: String = ""
        set(value) {
            field = value
            binding.textView.text = value
        }

    var textColor: Int = Color.BLACK
        set(value) {
            field = value
            binding.textView.setTextColor(value)
        }

    var textSize: Float = 16.sp
        set(value) {
            field = value
            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }

    var drawable: Drawable? = null
        set(value) {
            field = value
            binding.imageView.background = value
        }

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewPinKeyboardButtonBinding.inflate(inflater, this)
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PinKeyboardButtonView,
            0,
            0
        ).apply {

            try {
                btnBackgroundColor = getColor(
                    R.styleable.PinKeyboardButtonView_btnBackgroundColor,
                    btnBackgroundColor
                )
                text = getString(R.styleable.PinKeyboardButtonView_android_text).orEmpty()
                textColor = getColor(R.styleable.PinKeyboardButtonView_android_textColor, textColor)
                textSize = getDimension(
                    R.styleable.PinKeyboardButtonView_android_textSize, textSize
                )
                drawable = getDrawable(R.styleable.PinKeyboardButtonView_android_src)
            } finally {
                recycle()
            }
        }
    }
}