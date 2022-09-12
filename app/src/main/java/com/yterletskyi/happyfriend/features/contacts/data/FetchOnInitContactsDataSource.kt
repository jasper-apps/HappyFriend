package com.yterletskyi.happyfriend.features.contacts.data

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.net.toUri
import com.yterletskyi.happyfriend.common.BirthdayParser
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FetchOnInitContactsDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
    private val birthdayParser: BirthdayParser
) : ContactsDataSource {

    private val fullContactList: List<Contact> by lazy { queryContacts().orEmpty() }

    private val _contactsFlow: MutableStateFlow<List<Contact>> = MutableStateFlow(fullContactList)
    override val contactsFlow: Flow<List<Contact>> = _contactsFlow

    override fun initialize() {
        search("")
    }

    override fun search(query: String) {
        _contactsFlow.value = fullContactList.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    override fun destroy() {}

    private fun queryContacts(): List<Contact>? {
        return contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.PHOTO_THUMBNAIL_URI,
                ContactsContract.CommonDataKinds.Event.START_DATE
            ),
            "display_name is not null",
            null,
            "display_name"
        )?.use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map(::cursorToContact)
                .groupBy { it.id }
                .map { (_, contacts) ->
                    contacts
                        .singleOrNull { it.birthday != null }
                        ?: contacts.first()
                }
        }
    }

    private fun cursorToContact(cursor: Cursor): Contact {
        return Contact(
            id = cursor.getLong(0),
            name = cursor.getString(1),
            image = cursor.getString(2)
                ?.toUri()
                ?.let(::drawableFromUri),
            birthday = cursor.getString(3)?.let(birthdayParser::parse)
        )
    }

    private fun drawableFromUri(uri: Uri): Drawable? = contentResolver.openInputStream(uri)
        .use { Drawable.createFromStream(it, uri.toString()) }
}
