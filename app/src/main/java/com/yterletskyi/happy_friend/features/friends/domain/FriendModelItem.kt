package com.yterletskyi.happy_friend.features.friends.domain

import android.graphics.drawable.Drawable
import com.yterletskyi.happy_friend.common.list.ModelItem
import java.time.LocalDate

data class FriendModelItem(
    val id: String,
    val contactId: Long,
    val image: Drawable,
    val fullName: String,
    val birthday: LocalDate
) : ModelItem