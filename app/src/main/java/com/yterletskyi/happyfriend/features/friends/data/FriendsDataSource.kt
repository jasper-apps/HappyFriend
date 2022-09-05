package com.yterletskyi.happyfriend.features.friends.data

import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface FriendsDataSource {
    val friendsFlow: Flow<List<Friend>>
    suspend fun addFriend(friend: Friend)
    suspend fun removeFriend(contactId: Long)
    suspend fun updateFriend(id: String, position: Long)
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

class RoomFriendsDataSource(
    private val friendsDao: FriendsDao
) : FriendsDataSource {

    override val friendsFlow: Flow<List<Friend>> = friendsDao.getFriends()

    override suspend fun addFriend(friend: Friend) = friendsDao.addFriend(friend)

    override suspend fun updateFriend(id: String, position: Long) {
        friendsDao.updateFriend(id, position)
    }

    override suspend fun removeFriend(contactId: Long) = friendsDao.removeFriend(contactId)

    override suspend fun isFriend(contactId: Long): Boolean = friendsDao.isFriend(contactId)
}

class DemoFriendsDataSource(
    contactsDataSource: ContactsDataSource,
) : FriendsDataSource {

    private val dropEvery: Int = 3

    override val friendsFlow: Flow<List<Friend>> = contactsDataSource.contactsFlow
        .map { contacts ->
            contacts.mapIndexed { i, it ->
                if (i % dropEvery != 0) {
                    Friend(
                        id = it.id.toString(),
                        contactId = it.id,
                        position = i.toLong()
                    )
                } else null
            }.filterNotNull() + GlobalFriends.MyWishlistFriend + GlobalFriends.GeneralIdeas
        }

    override suspend fun addFriend(friend: Friend) {}

    override suspend fun removeFriend(contactId: Long) {}

    override suspend fun updateFriend(id: String, position: Long) {}

    override suspend fun isFriend(contactId: Long): Boolean {
        return contactId % dropEvery != 0L
    }
}