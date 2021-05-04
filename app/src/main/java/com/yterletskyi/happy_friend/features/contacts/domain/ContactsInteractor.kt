package com.yterletskyi.happy_friend.features.contacts.domain

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.yterletskyi.happy_friend.common.drawable.AvatarDrawable
import com.yterletskyi.happy_friend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happy_friend.features.contacts.data.initials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ContactsInteractor {
    fun getContacts(query: String = ""): Flow<List<ContactModelItem>>
}

class ContactsInteractorImpl @Inject constructor(
    private val context: Context,
    private val dataSource: ContactsDataSource
) : ContactsInteractor {

    override fun getContacts(query: String): Flow<List<ContactModelItem>> =
        dataSource.getContacts(query)
            .map {
                it.map {
                    ContactModelItem(
                        id = it.id,
                        image = it.imageUri
                            ?.let { drawableFromUri(it) }
                            ?: AvatarDrawable(it.initials),
                        fullName = it.name,
                        birthday = it.birthday
                    )
                }
            }

    private fun drawableFromUri(uri: Uri): Drawable = context.contentResolver.openInputStream(uri)
        .use { Drawable.createFromStream(it, uri.toString()) }

}