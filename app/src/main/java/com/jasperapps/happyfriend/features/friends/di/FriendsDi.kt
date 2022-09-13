package com.jasperapps.happyfriend.features.friends.di

import android.content.Context
import com.jasperapps.happyfriend.common.BirthdayFormatter
import com.jasperapps.happyfriend.features.contacts.data.ContactsDataSource
import com.jasperapps.happyfriend.features.friends.data.FriendsDataSource
import com.jasperapps.happyfriend.features.friends.domain.FriendsInteractor
import com.jasperapps.happyfriend.features.friends.domain.FriendsInteractorImpl
import com.jasperapps.happyfriend.features.settings.domain.GeneralIdeasController
import com.jasperapps.happyfriend.features.settings.domain.MyWishlistController
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
        birthdayFormatter: BirthdayFormatter,
        myWishlistController: MyWishlistController,
        generalIdeasController: GeneralIdeasController
    ): FriendsInteractor {
        return FriendsInteractorImpl(
            context,
            friendsDataSource,
            contactsDataSource,
            birthdayFormatter,
            myWishlistController,
            generalIdeasController,
        )
    }
}
