package com.jasperapps.happyfriend.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jasperapps.happyfriend.features.friends.data.Friend
import com.jasperapps.happyfriend.features.friends.data.FriendsDao
import com.jasperapps.happyfriend.features.ideas.data.Idea
import com.jasperapps.happyfriend.features.ideas.data.IdeasDao

@Database(
    entities = [Friend::class, Idea::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract val friendsDao: FriendsDao
    abstract val ideasDao: IdeasDao
}
