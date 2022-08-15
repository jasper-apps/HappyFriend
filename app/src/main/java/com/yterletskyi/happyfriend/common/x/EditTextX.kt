package com.yterletskyi.happyfriend.common.x

import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService

fun EditText.setTextNoTextWatcher(watcher: TextWatcher, text: CharSequence) {
    removeTextChangedListener(watcher)
    setText(text)
    addTextChangedListener(watcher)
}

fun EditText.focus() {
    requestFocus()
    setSelection(text.length)
    post { showKeyboard() }
}

fun EditText.showKeyboard() {
    val imm = getSystemService(context, InputMethodManager::class.java)
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}
