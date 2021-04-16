package com.yterletskyi.happy_friend.features.friends.data

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import javax.inject.Inject

interface FriendsDataSource {
    fun getFriends(): Flow<List<Friend>>
}

class FakeFriendsDataSource @Inject constructor() : FriendsDataSource {
    override fun getFriends(): Flow<List<Friend>> = flowOf(
        listOf(
            Friend(
                id = 1,
                firstName = "Yura",
                lastName = "Basa",
                birthday = LocalDate.of(1995, 6, 30)
            ),
            Friend(
                id = 2,
                firstName = "Ostap",
                lastName = "Holub",
                birthday = LocalDate.of(1997, 2, 6)
            ),
            Friend(
                id = 1,
                firstName = "Andrew",
                lastName = "Yaniv",
                birthday = LocalDate.of(1997, 1, 10)
            ),
            Friend(
                id = 1,
                firstName = "Taras",
                lastName = "Smakula",
                birthday = LocalDate.of(1997, 2, 9)
            )
        )
    )
}

class PhoneContactsDataSource(
    private val context: Context
) : FriendsDataSource {

    private val PROJECTION: Array<out String> = arrayOf(
        ContactsContract.Data.CONTACT_ID,
        ContactsContract.Data.DISPLAY_NAME,
        ContactsContract.Data.LOOKUP_KEY
    )

//    private val flow: MutableStateFlow<List<Friend>> = MutableStateFlow(emptyList())

    override fun getFriends(): Flow<List<Friend>> = flow {
        val friends = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            PROJECTION,
            null,
            null,
            null
        )?.use { cursor ->
            generateSequence { if (cursor.moveToNext()) cursor else null }
                .map {
                    cursorToFriend(it)
                }
                .toList()
        } ?: emptyList()

        emit(friends)
    }

//    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = CursorLoader(
//        context,
//        ContactsContract.Contacts.CONTENT_URI,
//        PROJECTION,
//        null,
//        null,
//        null
//    )
//
//
//    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
//        val friends = generateSequence { if (cursor.moveToNext()) cursor else null }
//            .map {
//                cursorToFriend(it)
//            }
//            .toList()
//    }
//
//    override fun onLoaderReset(loader: Loader<Cursor>) {
//        TODO("Not yet implemented")
//    }

    private fun cursorToFriend(cursor: Cursor): Friend = Friend(
        id = cursor.getLong(0),
        firstName = cursor.getString(1),
        lastName = cursor.getString(1),
        birthday = LocalDate.now()
    )

}