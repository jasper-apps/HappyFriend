package com.yterletskyi.happyfriend.common.di

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yterletskyi.happyfriend.features.friends.data.GlobalFriends

class PrepopulateMyGlobalIdeaList : RoomDatabase.Callback() {

    private val generalIdea = GlobalFriends.MyGeneralIdea

    override fun onCreate(db: SupportSQLiteDatabase) {
        val contentValues = ContentValues().apply {
            put("id", generalIdea.id)
            put("contact_id", generalIdea.contactId)
            put("position", generalIdea.position)
        }
        db.insert("friend", SQLiteDatabase.CONFLICT_ABORT, contentValues)
    }
}
