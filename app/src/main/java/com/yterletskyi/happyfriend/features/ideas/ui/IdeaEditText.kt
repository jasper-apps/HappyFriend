package com.yterletskyi.happyfriend.features.ideas.ui

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import com.yterletskyi.happyfriend.common.logger.Logger
import com.yterletskyi.happyfriend.common.logger.loggers
import com.yterletskyi.happyfriend.common.x.setTextNoTextWatcher

class IdeaEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatEditText(context, attrs) {

    private val logger: Logger by loggers()

    var onNewIdeaRequested: ((String) -> Unit)? = null
    var onRemoveIdeaRequested: (() -> Unit)? = null
    var onFocusChanged: ((Boolean) -> Unit)? = null

    private val newLineWatcher = NewLineTextWatcher(::onNewLineAdded)
    private val focusChangeListener = OnFocusChangeListener { view, isFocused ->
        onFocusChanged?.invoke(isFocused)
    }

    private fun onNewLineAdded(before: CharSequence, after: CharSequence) {
        logger.info("before: [$before] - after: [$after]")
        setTextNoTextWatcher(newLineWatcher, before)
        onNewIdeaRequested?.invoke(after.toString())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (text?.isEmpty() == true) {
                logger.info("backspace clicked on empty idea")
                onRemoveIdeaRequested?.invoke()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextChangedListener(newLineWatcher)
        onFocusChangeListener = focusChangeListener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeTextChangedListener(newLineWatcher)
        onFocusChangeListener = null
    }
}
