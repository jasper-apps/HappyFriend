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
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

interface ContactsDataSource {
    val contactsFlow: Flow<List<Contact>>
    fun search(query: String = "")
}

class FakeContactsDataSource : ContactsDataSource {

    private val list = listOf(
        Contact(
            id = 1,
            imageUri = null,
            name = "Yura Basa",
            birthday = LocalDate.of(1995, 6, 30)
        ),
        Contact(
            id = 2,
            imageUri = null,
            name = "Ostap Holub",
            birthday = LocalDate.of(1997, 2, 6)
        ),
        Contact(
            id = 3,
            imageUri = null,
            name = "Andre Yaniv",
            birthday = LocalDate.of(1997, 1, 10)
        ),
        Contact(
            id = 4,
            imageUri = null,
            name = "Taras Smakula",
            birthday = LocalDate.of(1997, 2, 9)
        )
    )

    private val _contactsFlow: MutableStateFlow<List<Contact>> = MutableStateFlow(list)
    override val contactsFlow: Flow<List<Contact>> = _contactsFlow

    override fun search(query: String) {
        _contactsFlow.value = list
    }
}

class PhoneContactsDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val birthdayParser: BirthdayParser,
) : ContactsDataSource {

    private val fullContactList: List<Contact> = queryContacts().orEmpty()

    private val _contactsFlow: MutableStateFlow<List<Contact>> = MutableStateFlow(fullContactList)
    override val contactsFlow: Flow<List<Contact>> = _contactsFlow

    override fun search(query: String) {
        _contactsFlow.value = fullContactList.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    private fun queryContacts(): List<Contact>? {
        return context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.Data.PHOTO_THUMBNAIL_URI,
                ContactsContract.CommonDataKinds.Event.START_DATE,
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
            imageUri = cursor.getString(2)?.toUri(),
            birthday = cursor.getString(3)?.let(birthdayParser::parse),
        )
    }

}

class PhoneContactsDataSource2 @Inject constructor(
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


@OptIn(ExperimentalTime::class)
class TimeMeasuredContactsDataSource(
    private val impl: ContactsDataSource
) : ContactsDataSource by impl {

    init {
        Log.i("info23", "initializing contacts data source")
    }

    override fun search(query: String) {
        Log.i("info23", "searching for <$query>")
        val time = measureTime { impl.search(query) }
        Log.i("info23", "getContacts with query=<$query> took <${time.inSeconds}> sec")
    }
}