package com.jasperapps.happyfriend.features.friends.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class InMemoryFriendsDataSource : FriendsDataSource {

    private val _friendsFlow: MutableStateFlow<List<Friend>> = MutableStateFlow(emptyList())
    override val friendsFlow: Flow<List<Friend>> = _friendsFlow

    override suspend fun addFriend(friend: Friend) {
        val friends = _friendsFlow.value
        val newFriends = friends
            .toMutableList()
            .apply { add(friend) }
        _friendsFlow.value = newFriends
    }

    override suspend fun updateFriend(id: String, position: Long) {
        val friends = _friendsFlow.value
        val oldFriendIndex = friends
            .indexOfFirst { it.id == id }
        val newFriend = friends[oldFriendIndex]
            .copy(position = position)

        val newFriends = friends
            .toMutableList()
            .apply { set(oldFriendIndex, newFriend) }
        _friendsFlow.value = newFriends
    }

    override suspend fun removeFriend(contactId: Long) {
        val friends = _friendsFlow.value
        val newFriends = friends
            .toMutableList()
            .apply { removeIf { it.contactId == contactId } }
        _friendsFlow.value = newFriends
    }

    override suspend fun isFriend(contactId: Long): Boolean = _friendsFlow.value
        .find { it.contactId == contactId } != null
}
