package com.yterletskyi.happyfriend.features.contacts.di

import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.contacts.domain.ContactsInteractor
import com.yterletskyi.happyfriend.features.contacts.domain.ContactsInteractorImpl
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ContactsDi {

    @Provides
    fun provideContactsInteractor(
        contactsDataSource: ContactsDataSource,
        friendsDataSource: FriendsDataSource,
        birthdayFormatter: BirthdayFormatter
    ): ContactsInteractor {
        return ContactsInteractorImpl(
            contactsDataSource,
            friendsDataSource,
            birthdayFormatter
        )
    }
}
