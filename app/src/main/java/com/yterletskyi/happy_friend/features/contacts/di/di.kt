package com.yterletskyi.happy_friend.features.contacts.di

import android.content.Context
import com.yterletskyi.happy_friend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happy_friend.features.contacts.data.PhoneContactsDataSource
import com.yterletskyi.happy_friend.features.contacts.domain.ContactsInteractor
import com.yterletskyi.happy_friend.features.contacts.domain.ContactsInteractorImpl
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class, FragmentComponent::class)
class ContactsDi {

    @Provides
    fun provideContactsDataSource(@ApplicationContext context: Context): ContactsDataSource {
        return PhoneContactsDataSource(context)
    }

    @Provides
    fun provideContactsInteractor(
        @ApplicationContext context: Context,
        contactsDataSource: ContactsDataSource,
        friendsDataSource: FriendsDataSource
    ): ContactsInteractor {
        return ContactsInteractorImpl(context, contactsDataSource, friendsDataSource)
    }

}