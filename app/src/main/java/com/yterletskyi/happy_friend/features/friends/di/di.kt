package com.yterletskyi.happy_friend.features.friends.di

import android.content.Context
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import com.yterletskyi.happy_friend.features.friends.data.PhoneContactsDataSource
import com.yterletskyi.happy_friend.features.friends.domain.FriendsInteractor
import com.yterletskyi.happy_friend.features.friends.domain.FriendsInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class FriendsDi {

    @Provides
    fun provideFriendsDataSource(@ApplicationContext context: Context): FriendsDataSource {
        return PhoneContactsDataSource(context)
    }

    @Provides
    fun provideFriendsInteractor(
        @ApplicationContext context: Context,
        dataSource: FriendsDataSource
    ): FriendsInteractor {
        return FriendsInteractorImpl(context, dataSource)
    }

}