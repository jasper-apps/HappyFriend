package com.yterletskyi.happyfriend.features.friends.di

import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractor
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class FriendsDi {

    @Provides
    fun provideFriendsInteractor(
        friendsDataSource: FriendsDataSource,
        contactsDataSource: ContactsDataSource,
        birthdayFormatter: BirthdayFormatter
    ): FriendsInteractor {
        return FriendsInteractorImpl(
            friendsDataSource,
            contactsDataSource,
            birthdayFormatter
        )
    }
}
