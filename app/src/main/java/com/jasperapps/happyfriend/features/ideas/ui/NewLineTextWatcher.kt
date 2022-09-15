package com.jasperapps.happyfriend.features.ideas.ui

import android.text.Editable
import android.text.TextWatcher

class NewLineTextWatcher(
    private val onNewLineEntered: (before: CharSequence, after: CharSequence) -> Unit,
) : TextWatcher {

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val newLineIndex = s.indexOf('\n')

        if (newLineIndex != -1) {
            val noNewLineString = removeNewLine(s)
            when (newLineIndex) {
                0 -> onNewLineEntered("", noNewLineString) // new line at start
                s.length - 1 -> onNewLineEntered(noNewLineString, "") // new line at the end
                else -> onNewLineEntered(
                    noNewLineString.substring(0, start),
                    noNewLineString.substring(start, s.length - 1)
                ) // new line in the middle
            }
        }
    }

    private fun removeNewLine(s: CharSequence): CharSequence {
        val newLineRegex = Regex("\n")
        return s.replace(newLineRegex, "")
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {}
}
