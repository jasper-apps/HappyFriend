package com.yterletskyi.happyfriend.features.friends.data

import kotlinx.coroutines.flow.Flow

interface FriendsDataSource {
    val friendsFlow: Flow<List<Friend>>
    suspend fun addFriend(friend: Friend)
    suspend fun removeFriend(contactId: Long)
    suspend fun updateFriend(id: String, position: Long)
    suspend fun isFriend(contactId: Long): Boolean
}


