package com.jasperapps.happyfriend.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.jasperapps.happyfriend.R
import com.jasperapps.happyfriend.databinding.ViewHappyToolbarBinding

class HappyToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _binding: ViewHappyToolbarBinding? = null
    private val binding: ViewHappyToolbarBinding get() = _binding!!

    var onBackClicked: (() -> Unit)? = null
        set(value) {
            with(binding.btnBack) {
                value
                    ?.let {
                        visibility = View.VISIBLE
                        setOnClickListener { it() }
                    }
                    ?: run { visibility = View.GONE }
            }
            field = value
        }

    var onActionClicked: (() -> Unit)? = null
        set(value) {
            with(binding.btnAction) {
                value
                    ?.let {
                        visibility = View.VISIBLE
                        setOnClickListener { it() }
                    }
                    ?: run { visibility = View.GONE }
            }
            field = value
        }

    var title: String = ""
        set(value) {
            binding.tvTitle.text = value
            field = value
        }

    var actionSrc: Int = 0
        set(value) {
            binding.btnAction.setImageResource(value)
            field = value
        }

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewHappyToolbarBinding.inflate(inflater, this, true)
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HappyToolbarView,
            0,
            0
        ).apply {

            try {
                title = getString(R.styleable.HappyToolbarView_title).orEmpty()
                actionSrc = getResourceId(R.styleable.HappyToolbarView_actionSrc, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}
