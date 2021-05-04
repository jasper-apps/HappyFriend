package com.yterletskyi.happy_friend.features.friends.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FriendsDataSource {
    fun getFriends(): Flow<List<Friend>>
}

class FakeFriendsDataSource : FriendsDataSource {
    override fun getFriends(): Flow<List<Friend>> = flow { emptyList<Friend>() }
}