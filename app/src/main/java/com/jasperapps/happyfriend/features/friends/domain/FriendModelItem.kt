package com.jasperapps.happyfriend.features.friends.domain

import android.graphics.drawable.Drawable
import com.jasperapps.happyfriend.common.list.ModelItem

data class FriendModelItem(
    val id: String,
    val contactId: Long,
    val image: Drawable,
    val fullName: String,
    val birthday: String,
    val position: Long,
) : ModelItem
