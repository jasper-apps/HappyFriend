package com.yterletskyi.happyfriend.features.ideas.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText
import com.yterletskyi.happyfriend.common.x.setTextNoTextWatcher

class IdeaEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatEditText(context, attrs) {

    var onNewIdeaRequested: ((String) -> Unit)? = null
    var onRemoveIdeaRequested: (() -> Unit)? = null
    private val newLineWatcher = NewLineTextWatcher(::onNewLineAdded)

    private fun onNewLineAdded(before: CharSequence, after: CharSequence) {
        Log.i("info24", "before: [$before] - after: [$after]")
        setTextNoTextWatcher(newLineWatcher, before)
        onNewIdeaRequested?.invoke(after.toString())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (text?.isEmpty() == true) {
                Log.i("info24", "backspace clicked on empty idea")
                onRemoveIdeaRequested?.invoke()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(newLineWatcher)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(newLineWatcher)
    }
}
