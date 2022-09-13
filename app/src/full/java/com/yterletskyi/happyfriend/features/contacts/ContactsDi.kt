package com.jasperapps.happyfriend.features.contacts

import android.content.ContentResolver
import com.jasperapps.happyfriend.common.BirthdayFormatter
import com.jasperapps.happyfriend.common.BirthdayParser
import com.jasperapps.happyfriend.features.contacts.data.Contact
import com.jasperapps.happyfriend.features.contacts.data.ContactsDataSource
import com.jasperapps.happyfriend.features.contacts.data.FetchBirthdaysOnInitContactsDataSource
import com.jasperapps.happyfriend.features.contacts.domain.ContactsInteractor
import com.jasperapps.happyfriend.features.contacts.domain.ContactsInteractorImpl
import com.jasperapps.happyfriend.features.friends.data.FriendsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.MutableStateFlow

@Module
@InstallIn(ViewModelComponent::class)
class ContactsDi {

    @Provides
    fun provideContactsDataSource(
        contentResolver: ContentResolver,
        initialContactsFlow: MutableStateFlow<List<Contact>>,
        birthdayParser: BirthdayParser
    ): ContactsDataSource {
        return FetchBirthdaysOnInitContactsDataSource(
            contentResolver,
            initialContactsFlow,
            birthdayParser
        )
    }

    @Provides
    fun provideContactsFlow(): MutableStateFlow<List<Contact>> = MutableStateFlow(emptyList())

    @Provides
    fun provideBirthdayParser(): BirthdayParser = BirthdayParser()

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
