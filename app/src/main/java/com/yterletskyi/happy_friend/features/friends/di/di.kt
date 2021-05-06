package com.yterletskyi.happy_friend.features.friends.di

import android.content.Context
import com.yterletskyi.happy_friend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
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
    fun provideFriendsInteractor(
        @ApplicationContext context: Context,
        friendsDataSource: FriendsDataSource,
        contactsDataSource: ContactsDataSource,
    ): FriendsInteractor {
        return FriendsInteractorImpl(context, friendsDataSource, contactsDataSource)
    }

}