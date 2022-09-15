package com.jasperapps.happyfriend.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.jasperapps.happyfriend.databinding.ViewSearchBinding

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: ViewSearchBinding? = null
    private val binding: ViewSearchBinding get() = _binding!!

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewSearchBinding.inflate(inflater, this, true)
    }

    var etInput = binding.etInput
    var btnClear = binding.btnClear

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}
