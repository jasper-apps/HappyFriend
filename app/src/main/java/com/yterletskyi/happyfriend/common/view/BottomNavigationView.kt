package com.yterletskyi.happyfriend.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.yterletskyi.happyfriend.databinding.ViewBottomNavBinding

class BottomNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: ViewBottomNavBinding? = null
    private val binding: ViewBottomNavBinding get() = _binding!!

    var onItemClickListener: ((Int) -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewBottomNavBinding.inflate(inflater, this)
    }

    init {
        with(binding) {
            btnFriends.setOnClickListener(::onChildClicked)
            btnSettings.setOnClickListener(::onChildClicked)
        }
    }

    init {
        val firstChild = getChildAt(0)
        firstChild?.let { handleSelection(it) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }

    private fun onChildClicked(view: View) {
        handleSelection(view)
        val index = indexOfChild(view)
        onItemClickListener?.invoke(index)
    }

    private fun handleSelection(view: View) {
        children.forEach { it.isSelected = false }
        view.isSelected = true
    }
}