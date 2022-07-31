package com.yterletskyi.happyfriend.common.x

import android.text.TextWatcher
import android.widget.EditText

fun EditText.setTextNoTextWatcher(watcher: TextWatcher, text: String) {
    removeTextChangedListener(watcher)
    setText(text)
    addTextChangedListener(watcher)
}

fun EditText.focus() {
    focus2()
}

fun EditText.focus2() {
    requestFocus()
    setSelection(text.length)
}
