package com.yterletskyi.happyfriend.features.friends.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface FriendsDataSource {
    val friendsFlow: Flow<List<Friend>>
    suspend fun addFriend(friend: Friend)
    suspend fun removeFriend(contactId: Long)
    suspend fun isFriend(contactId: Long): Boolean
}

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

class RoomFriendsDataSource(
    private val friendsDao: FriendsDao
) : FriendsDataSource {

    override val friendsFlow: Flow<List<Friend>> = friendsDao.getFriends()

    override suspend fun addFriend(friend: Friend) = friendsDao.addFriend(friend)

    override suspend fun removeFriend(contactId: Long) = friendsDao.removeFriend(contactId)

    override suspend fun isFriend(contactId: Long): Boolean = friendsDao.isFriend(contactId)
}
