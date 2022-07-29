package com.yterletskyi.happyfriend.features.friends.domain

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.common.drawable.AvatarDrawable
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.contacts.data.initials
import com.yterletskyi.happyfriend.features.friends.data.Friend
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

interface FriendsInteractor {
    val friendsFlow: Flow<List<FriendModelItem>>
    suspend fun addFriend(friendModel: FriendModelItem)
    suspend fun removeFriend(contactId: Long)
    suspend fun isFriend(contactId: Long): Boolean

    fun getFriend(id: String): Flow<FriendModelItem?> = friendsFlow
        .map { it.find { it.id == id } }
}

class FriendsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
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
                    image = co!!.imageUri
                        ?.let { drawableFromUri(it) }
                        ?: AvatarDrawable(co.initials),
                    fullName = co.name,
                    birthday = birthdayFormatter.format(co.birthday)
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

    private fun drawableFromUri(uri: Uri): Drawable? = context.contentResolver.openInputStream(uri)
        .use { Drawable.createFromStream(it, uri.toString()) }
}
