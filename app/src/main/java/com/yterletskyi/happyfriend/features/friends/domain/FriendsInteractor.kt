package com.yterletskyi.happyfriend.features.friends.domain

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.content.res.AppCompatResources
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.common.LifecycleComponent
import com.yterletskyi.happyfriend.common.drawable.AvatarDrawable
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.contacts.data.initials
import com.yterletskyi.happyfriend.features.friends.data.Friend
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import com.yterletskyi.happyfriend.features.friends.data.GlobalFriends
import com.yterletskyi.happyfriend.features.settings.domain.GeneralIdeasController
import com.yterletskyi.happyfriend.features.settings.domain.MyWishlistController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

interface FriendsInteractor : LifecycleComponent {
    val friendsFlow: Flow<List<FriendModelItem>>
    suspend fun addFriend(friendModel: FriendModelItem)
    suspend fun removeFriend(contactId: Long)
    suspend fun updateFriend(friendModel: FriendModelItem)
    suspend fun isFriend(contactId: Long): Boolean

    fun getFriend(id: String): Flow<FriendModelItem?> = friendsFlow
        .map { it.find { it.id == id } }
}

class FriendsInteractorImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val friendsDataSource: FriendsDataSource,
    contactsDataSource: ContactsDataSource,
    private val birthdayFormatter: BirthdayFormatter,
    private val myWishlistController: MyWishlistController,
    private val generalIdeasController: GeneralIdeasController
) : FriendsInteractor {

    override fun initialize() {
        myWishlistController.initialize()
        generalIdeasController.initialize()
    }

    override val friendsFlow: Flow<List<FriendModelItem>> = combine(
        friendsDataSource.friendsFlow,
        contactsDataSource.contactsFlow,
        myWishlistController.wishlistFlow,
        generalIdeasController.generalIdeaFlow
    ) { friends, contacts, myWishlistEnabled, myGeneralIdeaEnabled ->
        // map Contacts to Friends
        val friendModelItems = friends
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
            .toMutableList()

        // add 'my wishlist' item if needed
        if (myWishlistEnabled) {
            val myWishlistModel = friends
                .single { it.id == GlobalFriends.MyWishlistFriend.id }
            friendModelItems
                .apply {
                    val title = context.getString(R.string.title_my_wishlist_item)
                    val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_gift_box)
                    add(
                        FriendModelItem(
                            id = myWishlistModel.id,
                            contactId = myWishlistModel.contactId,
                            image = drawable ?: ColorDrawable(Color.BLACK),
                            fullName = title,
                            birthday = "",
                            position = myWishlistModel.position,
                        )
                    )
                }
        }

        if (myGeneralIdeaEnabled) {
            val generalIdeasModel = friends.single { it.id == GlobalFriends.GeneralIdeas.id }
            friendModelItems.apply {
                val title = context.getString(R.string.title_general_ideas_item)
                val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_gift_box)
                add(
                    FriendModelItem(
                        id = generalIdeasModel.id,
                        contactId = generalIdeasModel.contactId,
                        image = drawable ?: ColorDrawable(Color.BLACK),
                        fullName = title,
                        birthday = "",
                        position = generalIdeasModel.position,
                    )
                )
            }
        }

        friendModelItems.sortedBy { it.position }
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

    override fun destroy() {
        myWishlistController.destroy()
        generalIdeasController.destroy()
    }
}
