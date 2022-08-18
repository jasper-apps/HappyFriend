package com.yterletskyi.happyfriend.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.yterletskyi.happyfriend.databinding.ViewContactQueryBinding

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding : ViewContactQueryBinding? = null
    private val binding : ViewContactQueryBinding get() = _binding!!

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewContactQueryBinding.inflate(inflater, this, true)

    }

    var etInput = binding.etInput
    var btnClear = binding.btnClear

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

}
