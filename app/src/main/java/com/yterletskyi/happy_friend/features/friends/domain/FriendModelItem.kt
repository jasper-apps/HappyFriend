package com.yterletskyi.happy_friend.features.friends.domain

import com.yterletskyi.happy_friend.common.list.ModelItem
import java.time.LocalDate

data class FriendModelItem(
    val id: Long,
    val fullName: String,
    val birthday: LocalDate
) : ModelItem