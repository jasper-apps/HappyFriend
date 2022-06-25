package com.yterletskyi.happyfriend.features.friends.di

import android.content.Context
import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractor
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class)
class FriendsDi {

    @Provides
    fun provideFriendsInteractor(
        @ApplicationContext context: Context,
        friendsDataSource: FriendsDataSource,
        contactsDataSource: ContactsDataSource,
        birthdayFormatter: BirthdayFormatter
    ): FriendsInteractor {
        return FriendsInteractorImpl(
            context,
            friendsDataSource,
            contactsDataSource,
            birthdayFormatter
        )
    }
}
