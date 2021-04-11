package com.yterletskyi.happy_friend.features.friends.data

import java.time.LocalDate

data class Friend(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate
)