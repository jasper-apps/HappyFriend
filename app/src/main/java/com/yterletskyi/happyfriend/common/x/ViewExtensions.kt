package com.yterletskyi.happyfriend.common.x

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

val Int.dp: Int get() = (Resources.getSystem().displayMetrics.density * this).toInt()

val Float.dp: Float get() = (Resources.getSystem().displayMetrics.density * this)

inline val Float.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        Resources.getSystem().displayMetrics
    )

inline val Int.sp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

@ColorInt
fun Context.getThemeColor(@AttrRes attrRes: Int): Int = TypedValue()
    .apply { theme.resolveAttribute(attrRes, this, true) }
    .data
