package com.yterletskyi.happyfriend.features.contacts

import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.common.BirthdayParser
import com.yterletskyi.happyfriend.features.contacts.data.Contact
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.contacts.domain.ContactsInteractor
import com.yterletskyi.happyfriend.features.contacts.domain.ContactsInteractorImpl
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.MutableStateFlow

@Module
@InstallIn(ViewModelComponent::class)
class ContactsDi {

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
