package com.yterletskyi.happyfriend.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yterletskyi.happyfriend.features.friends.data.Friend
import com.yterletskyi.happyfriend.features.friends.data.FriendsDao
import com.yterletskyi.happyfriend.features.ideas.data.Idea
import com.yterletskyi.happyfriend.features.ideas.data.IdeasDao

@Database(entities = [Friend::class, Idea::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val friendsDao: FriendsDao
    abstract val ideasDao: IdeasDao
}
