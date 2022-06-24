package com.yterletskyi.happy_friend.features.contacts.domain

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.yterletskyi.happy_friend.common.BirthdayFormatter
import com.yterletskyi.happy_friend.common.drawable.AvatarDrawable
import com.yterletskyi.happy_friend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happy_friend.features.contacts.data.initials
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface ContactsInteractor {
    val contactsFlow: Flow<List<ContactModelItem>>
    fun search(query: String = "")
}

class ContactsInteractorImpl @Inject constructor(
    private val context: Context,
    private val contactsDataSource: ContactsDataSource,
    friendsDataSource: FriendsDataSource,
    private val birthdayFormatter: BirthdayFormatter,
) : ContactsInteractor {

    override val contactsFlow: Flow<List<ContactModelItem>> = combine(
        contactsDataSource.contactsFlow, friendsDataSource.friendsFlow
    ) { contacts, friends ->
        contacts
            .map { co ->
                ContactModelItem(
                    id = co.id,
                    image = co.imageUri
                        ?.let { drawableFromUri(it) }
                        ?: AvatarDrawable(co.initials),
                    fullName = co.name,
                    birthday = birthdayFormatter.format(co.birthday),
                    isFriend = friends.find { fr -> fr.contactId == co.id } != null
                )
            }
    }

    override fun search(query: String) = contactsDataSource.search(query)

    private fun drawableFromUri(uri: Uri): Drawable = context.contentResolver.openInputStream(uri)
        .use { Drawable.createFromStream(it, uri.toString()) }
}