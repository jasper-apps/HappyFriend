package com.yterletskyi.happy_friend.features.friends.di

import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import com.yterletskyi.happy_friend.features.friends.data.InMemoryFriendsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GlobalDi {

    @Provides
    @Singleton
    fun provideFriendsDataSource(): FriendsDataSource {
        return InMemoryFriendsDataSource()
    }

}