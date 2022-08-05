package com.yterletskyi.happyfriend.features.ideas.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import com.yterletskyi.happyfriend.common.x.setTextNoTextWatcher

class IdeaEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatEditText(context, attrs) {

    var onNewIdeaRequested: ((String) -> Unit)? = null
    private val watcher = NewLineTextWatcher(::onNewLineAdded)

    private fun onNewLineAdded(before: CharSequence, after: CharSequence) {
        Log.i("info24", "before: [$before] - after: [$after]")
        setTextNoTextWatcher(watcher, before)
        onNewIdeaRequested?.invoke(after.toString())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(watcher)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(watcher)
    }
}
