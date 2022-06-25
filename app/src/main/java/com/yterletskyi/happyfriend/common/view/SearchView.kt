package com.yterletskyi.happyfriend.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.x.dp

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setBackgroundResource(R.drawable.background_search_view)
        setHint(R.string.search_hint_contacts)
        inputType = EditorInfo.TYPE_CLASS_TEXT

        TypedValue().let {
            context.theme.resolveAttribute(R.attr.textColorSecondary, it, true)
            setHintTextColor(it.data)
        }
        setTextColor(ContextCompat.getColor(context, R.color.black))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        setPadding(16.dp)
    }
}
