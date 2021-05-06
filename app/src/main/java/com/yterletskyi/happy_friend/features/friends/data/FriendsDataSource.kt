package com.yterletskyi.happy_friend.features.friends.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface FriendsDataSource {
    fun getFriends(): Flow<List<Friend>>
    suspend fun addFriend(friend: Friend)
    suspend fun removeFriend(id: Long)
    suspend fun isFriend(id: Long): Boolean
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

    override suspend fun removeFriend(id: Long) {
        val friends = flow.value
        val newFriends = friends
            .toMutableList()
            .apply { removeIf { it.contactId == id } }
        flow.value = newFriends
    }

    override suspend fun isFriend(id: Long): Boolean = flow.value
        .find { it.contactId == id } != null

}