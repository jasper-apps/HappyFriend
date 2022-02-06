package com.yterletskyi.happy_friend.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.setPadding
import com.yterletskyi.happy_friend.R
import com.yterletskyi.happy_friend.common.x.dp


class SearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setBackgroundResource(R.drawable.background_search_view)
        setHint(R.string.search_hint_contacts)
        inputType = EditorInfo.TYPE_CLASS_TEXT

        TypedValue().let {
            context.theme.resolveAttribute(R.attr.textColorPrimary, it, true)
            setTextColor(it.data)
        }
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        setPadding(16.dp)

    }

}