package com.jasperapps.happyfriend.features.friends.domain

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.content.res.AppCompatResources
import com.jasperapps.happyfriend.R
import com.jasperapps.happyfriend.common.BirthdayFormatter
import com.jasperapps.happyfriend.common.LifecycleComponent
import com.jasperapps.happyfriend.common.drawable.AvatarInitialsDrawable
import com.jasperapps.happyfriend.common.drawable.RoundDrawable
import com.jasperapps.happyfriend.features.contacts.data.ContactsDataSource
import com.jasperapps.happyfriend.features.contacts.data.initials
import com.jasperapps.happyfriend.features.friends.data.Friend
import com.jasperapps.happyfriend.features.friends.data.FriendsDataSource
import com.jasperapps.happyfriend.features.friends.data.GlobalFriends
import com.jasperapps.happyfriend.features.settings.domain.GeneralIdeasController
import com.jasperapps.happyfriend.features.settings.domain.MyWishlistController
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
    private val contactsDataSource: ContactsDataSource,
    private val birthdayFormatter: BirthdayFormatter,
    private val myWishlistController: MyWishlistController,
    private val generalIdeasController: GeneralIdeasController
) : FriendsInteractor {

    override fun initialize() {
        myWishlistController.initialize()
        generalIdeasController.initialize()
        contactsDataSource.initialize()
    }

    override val friendsFlow: Flow<List<FriendModelItem>> = combine(
        friendsDataSource.friendsFlow,
        contactsDataSource.contactsFlow,
        myWishlistController.wishlistFlow,
        generalIdeasController.generalIdeasFlow
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
                    image = RoundDrawable(
                        co!!.image ?: AvatarInitialsDrawable(co.initials)
                    ),
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
                    val drawable = AppCompatResources.getDrawable(
                        context,
                        R.drawable.ic_round_self_improvement_24
                    )
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
                val drawable =
                    AppCompatResources.getDrawable(context, R.drawable.ic_outline_lightbulb_24)
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
        contactsDataSource.destroy()
    }
}
