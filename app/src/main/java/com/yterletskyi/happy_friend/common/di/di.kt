package com.yterletskyi.happy_friend.common.di

import android.content.Context
import androidx.room.Room
import com.yterletskyi.happy_friend.App
import com.yterletskyi.happy_friend.common.BirthdayFormatter
import com.yterletskyi.happy_friend.common.BirthdayFormatterImpl
import com.yterletskyi.happy_friend.common.data.AppDatabase
import com.yterletskyi.happy_friend.features.friends.data.FriendsDao
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import com.yterletskyi.happy_friend.features.friends.data.RoomFriendsDataSource
import com.yterletskyi.happy_friend.features.ideas.data.IdeasDao
import com.yterletskyi.happy_friend.features.ideas.data.IdeasDataSource
import com.yterletskyi.happy_friend.features.ideas.data.RoomIdeasDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GlobalDi {

    @Provides
    @Singleton
    fun provideApp(@ApplicationContext context: Context): App = context as App

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "happy-friend")
            .build()
    }

    @Provides
    fun provideIdeasDao(database: AppDatabase): IdeasDao {
        return database.ideasDao
    }

    @Provides
    fun provideFriendsDao(database: AppDatabase): FriendsDao {
        return database.friendsDao
    }

    @Provides
    @Singleton
    fun provideFriendsDataSource(friendsDao: FriendsDao): FriendsDataSource {
        return RoomFriendsDataSource(friendsDao)
    }

    @Provides
    @Singleton
    fun provideIdeasDataSource(ideasDao: IdeasDao): IdeasDataSource {
        return RoomIdeasDataSource(ideasDao)
    }

    @Provides
    fun provideBirthdayFormatter(): BirthdayFormatter {
        return BirthdayFormatterImpl()
    }
}