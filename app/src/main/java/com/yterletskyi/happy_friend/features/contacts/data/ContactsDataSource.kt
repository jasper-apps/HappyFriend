package com.yterletskyi.happy_friend.features.contacts.data


import android.content.Context
import android.database.Cursor
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

    private val _contactsFlow: MutableStateFlow<List<Contact>> = MutableStateFlow(emptyList())
    override val contactsFlow: Flow<List<Contact>> = _contactsFlow

    init {
        search("")
    }

    override fun search(query: String) {
        val contacts = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            PROJECTION_BIRTHDAY,
            SELECTION_STRING,
            emptyArray(),
            SORT_ORDER
        )?.use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map(::cursorToContact)
                .groupBy { it.id }
                .map { (_, contacts) ->
                    contacts
                        .singleOrNull { it.birthday != null }
                        ?: contacts.first()
                }
                .filter { it.name.contains(query, ignoreCase = true) }
        }
            ?.toList()
            .orEmpty()

        _contactsFlow.value = contacts
    }

    private fun cursorToContact(cursor: Cursor): Contact {
        return Contact(
            id = cursor.getLong(0),
            name = cursor.getString(1),
            imageUri = cursor.getString(2)?.toUri(),
            birthday = cursor.getString(3)?.let(birthdayParser::parse),
        )
    }

    private companion object {
        const val SELECTION_STRING = "display_name is not null"
        const val SORT_ORDER = "display_name"

        val PROJECTION_BIRTHDAY: Array<String> = arrayOf(
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.Data.PHOTO_THUMBNAIL_URI,
            ContactsContract.CommonDataKinds.Event.START_DATE,
        )
    }
}

@OptIn(ExperimentalTime::class)
class TimeMeasuredContactsDataSource(
    private val impl: ContactsDataSource
) : ContactsDataSource by impl {

    override fun search(query: String) {
        val time = measureTime { impl.search(query) }
        Log.i("info23", "getContacts with query=<$query> took <${time.inSeconds}> sec")
    }
}