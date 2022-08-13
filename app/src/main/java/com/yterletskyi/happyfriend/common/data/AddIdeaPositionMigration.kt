package com.yterletskyi.happyfriend.common.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class AddIdeaPositionMigration : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE idea ADD COLUMN position TEXT NOT NULL DEFAULT 'null'")
        database.execSQL("UPDATE idea SET position = id WHERE position = 'null'")
    }
}