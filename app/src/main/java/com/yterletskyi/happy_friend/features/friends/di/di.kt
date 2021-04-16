package com.yterletskyi.happy_friend.features.friends.di

import com.yterletskyi.happy_friend.features.friends.data.FakeFriendsDataSource
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import com.yterletskyi.happy_friend.features.friends.domain.FriendsInteractor
import com.yterletskyi.happy_friend.features.friends.domain.FriendsInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class FriendsDi {

    @Binds
    abstract fun provideFriendsDataSource(impl: FakeFriendsDataSource): FriendsDataSource

    @Binds
    abstract fun provideFriendsInteractor(impl: FriendsInteractorImpl): FriendsInteractor

}