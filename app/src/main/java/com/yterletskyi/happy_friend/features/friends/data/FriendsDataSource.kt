package com.yterletskyi.happy_friend.features.friends.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface FriendsDataSource {
    fun getFriends(): Flow<List<Friend>>
    suspend fun addFriend(friend: Friend)
}

class InMemoryFriendsDataSource : FriendsDataSource {

    private val flow: MutableStateFlow<List<Friend>> = MutableStateFlow(emptyList())

    override fun getFriends(): Flow<List<Friend>> = flow

    override suspend fun addFriend(friend: Friend) {
        val friends = flow.value
        val newFriends = friends
            .toMutableList()
            .apply { add(friend) }
        flow.value = newFriends
    }

}