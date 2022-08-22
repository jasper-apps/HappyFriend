package com.yterletskyi.happyfriend.common.di

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yterletskyi.happyfriend.features.friends.data.GlobalFriends

class PrepopulateMyWishlistFriend : RoomDatabase.Callback() {

    private val myWishlistFriend = GlobalFriends.MyWishlistFriend

    override fun onCreate(db: SupportSQLiteDatabase) {
        val contentValues = ContentValues().apply {
            put("id", myWishlistFriend.id)
            put("contact_id", myWishlistFriend.contactId)
            put("position", myWishlistFriend.position)
        }
        db.insert("friend", SQLiteDatabase.CONFLICT_ABORT, contentValues)
    }
}
