package com.yterletskyi.happyfriend.features.contacts.domain

import android.graphics.drawable.Drawable
import com.yterletskyi.happyfriend.common.list.ModelItem

data class ContactModelItem(
    val id: Long,
    val image: Drawable,
    val fullName: String,
    val birthday: String,
    val isFriend: Boolean
) : ModelItem
