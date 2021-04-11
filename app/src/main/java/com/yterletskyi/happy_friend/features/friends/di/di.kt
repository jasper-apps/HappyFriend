package com.yterletskyi.happy_friend.features.friends.di

import com.yterletskyi.happy_friend.features.friends.data.FakeFriendsDataSource
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class FriendsDi {

    @Binds
    abstract fun provideFriendsDataSource(impl: FakeFriendsDataSource): FriendsDataSource

}