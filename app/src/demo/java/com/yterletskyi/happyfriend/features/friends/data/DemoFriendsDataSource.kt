package com.yterletskyi.happyfriend.features.friends.data

import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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