package com.yterletskyi.happyfriend.features.friends.data

import kotlinx.coroutines.flow.Flow

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
