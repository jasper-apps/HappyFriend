package com.yterletskyi.happy_friend.features.friends.data

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.core.net.toUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

interface FriendsDataSource {
    fun getFriends(query: String): Flow<List<Friend>>
}

class FakeFriendsDataSource @Inject constructor() : FriendsDataSource {
    override fun getFriends(query: String): Flow<List<Friend>> = flowOf(
        listOf(
            Friend(
                id = 1,
                imageUri = null,
                name = "Yura Basa",
                birthday = LocalDate.of(1995, 6, 30)
            ),
            Friend(
                id = 2,
                imageUri = null,
                name = "Ostap Holub",
                birthday = LocalDate.of(1997, 2, 6)
            ),
            Friend(
                id = 3,
                imageUri = null,
                name = "Andre Yanivw",
                birthday = LocalDate.of(1997, 1, 10)
            ),
            Friend(
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
) : FriendsDataSource {

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

    private val flow: MutableStateFlow<List<Friend>> = MutableStateFlow(
        emptyList()
    )

    override fun getFriends(query: String): Flow<List<Friend>> {
        val whereParams = arrayOf("%${query}%")

        val friends = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            WHERE_STRING,
            whereParams,
            null
        )?.use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map {
                    cursorToFriend(it)
                }
                .toList()
        } ?: emptyList()

        return flow.also { it.tryEmit(friends) }
    }

    private fun cursorToFriend(cursor: Cursor): Friend = Friend(
        id = cursor.getLong(0),
        name = cursor.getString(1),
        imageUri = cursor.getString(2)?.toUri(),
        birthday = LocalDate.now()
    )

}