package com.yterletskyi.happyfriend.common.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yterletskyi.happyfriend.features.friends.data.GlobalFriends

class AddMyWishlistMigration : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        val friend = GlobalFriends.myWishlistFriend
        val contentValues = ContentValues().apply {
            put("id", friend.id)
            put("contact_id", friend.contactId)
            put("position", friend.position)
        }
        database.insert("friend", SQLiteDatabase.CONFLICT_ABORT, contentValues)
    }
}