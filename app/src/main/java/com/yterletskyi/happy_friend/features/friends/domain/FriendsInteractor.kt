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
import java.util.*
import javax.inject.Inject

interface FriendsInteractor {
    fun getFriends(): Flow<List<FriendModelItem>>
    suspend fun addFriend(friendModel: FriendModelItem)
    suspend fun removeFriend(contactId: Long)
    suspend fun isFriend(contactId: Long): Boolean
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
                    image = co!!.imageUri
                        ?.let { drawableFromUri(it) }
                        ?: AvatarDrawable(co.initials),
                    fullName = co.name,
                    birthday = co.birthday
                )
            }
    }


    override suspend fun addFriend(friendModel: FriendModelItem) {
        val friend = Friend(
            id = UUID.randomUUID().toString(),
            contactId = friendModel.contactId
        )
        friendsDataSource.addFriend(friend)
    }

    override suspend fun removeFriend(contactId: Long) {
        friendsDataSource.removeFriend(contactId)
    }

    override suspend fun isFriend(contactId: Long): Boolean {
        return friendsDataSource.isFriend(contactId)
    }

    private fun drawableFromUri(uri: Uri): Drawable = context.contentResolver.openInputStream(uri)
        .use { Drawable.createFromStream(it, uri.toString()) }

}