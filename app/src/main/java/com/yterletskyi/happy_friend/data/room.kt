package com.yterletskyi.happy_friend.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yterletskyi.happy_friend.features.friends.data.Friend
import com.yterletskyi.happy_friend.features.friends.data.FriendsDao
import com.yterletskyi.happy_friend.features.ideas.data.Idea
import com.yterletskyi.happy_friend.features.ideas.data.IdeasDao

@Database(entities = [Friend::class, Idea::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val friendsDao: FriendsDao
    abstract val ideasDao: IdeasDao
}