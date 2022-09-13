package com.jasperapps.happyfriend.features.contacts.data

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.net.toUri
import com.jasperapps.happyfriend.common.BirthdayParser
import com.jasperapps.happyfriend.common.logger.Logger
import com.jasperapps.happyfriend.common.logger.logcatLogger
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FetchBirthdaysOnInitContactsDataSource @Inject constructor(
    private val contentResolver: ContentResolver,
    private val initialContactsFlow: MutableStateFlow<List<Contact>>,
    private val birthdayParser: BirthdayParser
) : ContactsDataSource {

    private val logger: Logger by logcatLogger()

    private val contactBirthdayMap by lazy { queryBirthdays() }

    override val contactsFlow: Flow<List<Contact>> = initialContactsFlow

    override fun initialize() {
        search("")
    }

    override fun search(query: String) {
        val list = mutableListOf<Contact>()
        val uri = when {
            query.isBlank() -> ContactsContract.Contacts.CONTENT_URI
            else -> Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, query)
        }
        contentResolver.query(
            uri,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
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
        logger.info("returned ${list.size} contacts")
        initialContactsFlow.value = list
    }

    override fun destroy() {}

    private fun queryBirthdays(): Map<Long, LocalDate?> {
        val map = mutableMapOf<Long, LocalDate?>()
        contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
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
            image = cursor.getString(2)
                ?.toUri()
                ?.let(::drawableFromUri),
            birthday = contactBirthdayMap[id]
        )
    }

    private fun drawableFromUri(uri: Uri): Drawable? = contentResolver.openInputStream(uri)
        .use { Drawable.createFromStream(it, uri.toString()) }
}
