package com.yterletskyi.happy_friend.features.contacts.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.core.net.toUri
import com.yterletskyi.happy_friend.common.BirthdayParser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import javax.inject.Inject

class FetchBirthdaysOnInitContactsDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val birthdayParser: BirthdayParser,
) : ContactsDataSource {

    private var contactBirthdayMap = queryBirthdays()

    private val _contactsFlow: MutableStateFlow<List<Contact>> = MutableStateFlow(emptyList())
    override val contactsFlow: Flow<List<Contact>> = _contactsFlow

    init {
        search("")
    }

    override fun search(query: String) {
        val list = mutableListOf<Contact>()
        val uri = when {
            query.isBlank() -> ContactsContract.Contacts.CONTENT_URI
            else -> Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, query)
        }
        context.contentResolver.query(
            uri,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ),
            "display_name is not null",
            null,
            "display_name"
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val contact = cursorToContact(cursor)
                list.add(contact)
            }
        }
        Log.i("info23", "returned ${list.size} contacts")
        _contactsFlow.value = list
    }

    private fun queryBirthdays(): Map<Long, LocalDate?> {
        val map = mutableMapOf<Long, LocalDate?>()
        context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE,
            ),
            ContactsContract.Data.MIMETYPE + "= ? AND " +
                    ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                    ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
            arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(0)
                val birthday = cursor.getString(1).let(birthdayParser::parse)
                map[id] = birthday
            }
        }
        return map
    }

    private fun cursorToContact(cursor: Cursor): Contact {
        val id = cursor.getLong(0)
        return Contact(
            id = id,
            name = cursor.getString(1),
            imageUri = cursor.getString(2)?.toUri(),
            birthday = contactBirthdayMap.get(id)
        )
    }
}
