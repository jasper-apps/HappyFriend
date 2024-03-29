package com.jasperapps.happyfriend.common.di

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jasperapps.happyfriend.features.friends.data.GlobalFriends

class PrepopulateGeneralIdeasList : RoomDatabase.Callback() {

    private val generalIdeas = GlobalFriends.GeneralIdeas

    override fun onCreate(db: SupportSQLiteDatabase) {
        val contentValues = ContentValues().apply {
            put("id", generalIdeas.id)
            put("contact_id", generalIdeas.contactId)
            put("position", generalIdeas.position)
        }
        db.insert("friend", SQLiteDatabase.CONFLICT_ABORT, contentValues)
    }
}
