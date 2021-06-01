package com.yterletskyi.happy_friend.features.contacts.data


import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.core.net.toUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

interface ContactsDataSource {
    fun getContacts(query: String = ""): Flow<List<Contact>>
}

class FakeContactsDataSource @Inject constructor() : ContactsDataSource {
    override fun getContacts(query: String): Flow<List<Contact>> = flowOf(
        listOf(
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
                name = "Andre Yanivw",
                birthday = LocalDate.of(1997, 1, 10)
            ),
            Contact(
                id = 4,
                imageUri = null,
                name = "Taras Smakula",
                birthday = LocalDate.of(1997, 2, 9)
            )
        )
    )
}

class PhoneContactsDataSource @Inject constructor(
    private val context: Context
) : ContactsDataSource {

    companion object {
        private const val WHERE_STRING = "display_name LIKE ?"

        // TODO: 4/24/21 add birthday field
        private val PROJECTION: Array<String> = arrayOf(
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.Data.PHOTO_THUMBNAIL_URI,
            ContactsContract.Data.LOOKUP_KEY
        )
    }

    private val flow: MutableStateFlow<List<Contact>> = MutableStateFlow(
        emptyList()
    )

    override fun getContacts(query: String): Flow<List<Contact>> {
        val whereParams = arrayOf("%${query}%")

        val contacts = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            WHERE_STRING,
            whereParams,
            null
        )?.use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map {
                    cursorToContact(it)
                }
                .toList()
        } ?: emptyList()

        return flow.also { it.value = contacts }
    }

    private fun cursorToContact(cursor: Cursor): Contact = Contact(
        id = cursor.getLong(0),
        name = cursor.getString(1),
        imageUri = cursor.getString(2)?.toUri(),
        birthday = null
    )

}
