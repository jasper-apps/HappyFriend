package com.yterletskyi.happy_friend.features.friends.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

interface FriendsDataSource {
    fun getFriends(): Flow<List<Friend>>
}

class FakeFriendsDataSource @Inject constructor() : FriendsDataSource {
    override fun getFriends(): Flow<List<Friend>> = flowOf(
        listOf(
            Friend(
                id = 1,
                firstName = "Yura",
                lastName = "Basa",
                birthday = LocalDate.of(1995, 6, 30)
            ),
            Friend(
                id = 2,
                firstName = "Ostap",
                lastName = "Holub",
                birthday = LocalDate.of(1997, 2, 6)
            ),
            Friend(
                id = 1,
                firstName = "Andrew",
                lastName = "Yaniv",
                birthday = LocalDate.of(1997, 1, 10)
            ),
            Friend(
                id = 1,
                firstName = "Taras",
                lastName = "Smakula",
                birthday = LocalDate.of(1997, 2, 9)
            )
        )
    )
}