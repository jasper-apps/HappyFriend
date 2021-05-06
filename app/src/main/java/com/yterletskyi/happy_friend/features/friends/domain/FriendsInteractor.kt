package com.yterletskyi.happy_friend.features.friends.domain

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.yterletskyi.happy_friend.common.drawable.AvatarDrawable
import com.yterletskyi.happy_friend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happy_friend.features.contacts.data.initials
import com.yterletskyi.happy_friend.features.friends.data.Friend
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface FriendsInteractor {
    fun getFriends(): Flow<List<FriendModelItem>>
    suspend fun addFriend(friendModel: FriendModelItem)
    suspend fun removeFriend(id: Long)
    suspend fun isFriend(id: Long): Boolean
}

class FriendsInteractorImpl @Inject constructor(
    private val context: Context,
    private val friendsDataSource: FriendsDataSource,
    private val contactsDataSource: ContactsDataSource
) : FriendsInteractor {

    override fun getFriends(): Flow<List<FriendModelItem>> = combine(
        friendsDataSource.getFriends(), contactsDataSource.getContacts()
    ) { friends, contacts ->
        friends
            .mapNotNull { fr ->
                contacts.find { co -> co.id == fr.contactId }
            }
            .map {
                FriendModelItem(
                    id = it.id,
                    image = it.imageUri
                        ?.let { drawableFromUri(it) }
                        ?: AvatarDrawable(it.initials),
                    fullName = it.name,
                    birthday = it.birthday
                )
            }
    }


    override suspend fun addFriend(friendModel: FriendModelItem) {
        val friend = Friend(contactId = friendModel.id)
        friendsDataSource.addFriend(friend)
    }

    override suspend fun removeFriend(id: Long) {
        friendsDataSource.removeFriend(id)
    }

    override suspend fun isFriend(id: Long): Boolean {
        return friendsDataSource.isFriend(id)
    }

    private fun drawableFromUri(uri: Uri): Drawable = context.contentResolver.openInputStream(uri)
        .use { Drawable.createFromStream(it, uri.toString()) }

}