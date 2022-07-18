package com.yterletskyi.happyfriend.features.ideas.ui

import android.text.Editable
import android.text.TextWatcher

class NewLineTextWatcher(
    private val onNewLineEntered: (before: String, after: String) -> Unit,
) : TextWatcher {

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (before != 0 || s[start] != '\n') return

        val newLineRegex = Regex("\n")
        val trimmed = s.replace(newLineRegex, "")

        when (start) {
            0 -> onNewLineEntered("", trimmed) // new line at start
            s.length - 1 -> onNewLineEntered(trimmed, "") // new line at the end
            else -> onNewLineEntered(
                trimmed.substring(0, start),
                trimmed.substring(start, s.length - 1)
            ) // new line in the middle
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}
}