package com.yterletskyi.happyfriend.features.friends.domain

import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.common.drawable.AvatarDrawable
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.contacts.data.initials
import com.yterletskyi.happyfriend.features.friends.data.Friend
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

interface FriendsInteractor {
    val friendsFlow: Flow<List<FriendModelItem>>
    suspend fun addFriend(friendModel: FriendModelItem)
    suspend fun removeFriend(contactId: Long)
    suspend fun updateFriend(friendModel: FriendModelItem)
    suspend fun isFriend(contactId: Long): Boolean

    fun getFriend(id: String): Flow<FriendModelItem?> = friendsFlow
        .map { it.find { it.id == id } }
}

class FriendsInteractorImpl @Inject constructor(
    private val friendsDataSource: FriendsDataSource,
    contactsDataSource: ContactsDataSource,
    private val birthdayFormatter: BirthdayFormatter
) : FriendsInteractor {

    override val friendsFlow: Flow<List<FriendModelItem>> = combine(
        friendsDataSource.friendsFlow,
        contactsDataSource.contactsFlow
    ) { friends, contacts ->
        friends
            .map { fr ->
                fr to contacts.find { co -> co.id == fr.contactId }
            }
            .filter {
                it.second != null
            }
            .map { (fr, co) ->
                FriendModelItem(
                    id = fr.id,
                    contactId = fr.contactId,
                    image = co!!.image ?: AvatarDrawable(co.initials),
                    fullName = co.name,
                    birthday = birthdayFormatter.format(co.birthday),
                    position = fr.position,
                )
            }
    }

    override suspend fun addFriend(friendModel: FriendModelItem) {
        val friend = Friend(
            id = friendModel.id,
            contactId = friendModel.contactId,
            position = friendModel.position,
        )
        friendsDataSource.addFriend(friend)
    }

    override suspend fun updateFriend(friendModel: FriendModelItem) {
        friendsDataSource.updateFriend(friendModel.id, friendModel.position)
    }

    override suspend fun removeFriend(contactId: Long) {
        friendsDataSource.removeFriend(contactId)
    }

    override suspend fun isFriend(contactId: Long): Boolean {
        return friendsDataSource.isFriend(contactId)
    }
}
