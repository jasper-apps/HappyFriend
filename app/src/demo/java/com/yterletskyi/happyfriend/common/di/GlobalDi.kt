package com.yterletskyi.happyfriend.features.contacts.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.preference.PreferenceManager
import androidx.room.Room
import com.yterletskyi.happyfriend.App
import com.yterletskyi.happyfriend.BuildConfig
import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.common.LocalizedBirthdayFormatter
import com.yterletskyi.happyfriend.common.data.AppDatabase
import com.yterletskyi.happyfriend.common.data.FromAssetsDataSource
import com.yterletskyi.happyfriend.common.di.PrepopulateGeneralIdeasList
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.common.di.PrepopulateMyWishlistFriend
import com.yterletskyi.happyfriend.features.contacts.data.DemoContactsDataSource
import com.yterletskyi.happyfriend.features.friends.data.DemoFriendsDataSource
import com.yterletskyi.happyfriend.features.friends.data.FriendsDao
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import com.yterletskyi.happyfriend.features.ideas.data.DemoIdeasDataSource
import com.yterletskyi.happyfriend.features.ideas.data.IdeasDao
import com.yterletskyi.happyfriend.features.ideas.data.IdeasDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GlobalAi {

    @Provides
    fun provideContactsDataSource(
        assetManager: AssetManager
    ): ContactsDataSource {
        return DemoContactsDataSource(
            fromAssetsDataSource = FromAssetsDataSource(
                path = BuildConfig.ASSET_NAME_CONTACTS,
                assets = assetManager,
            )
        )
    }

    @Provides
    fun provideAssetsManager(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

    @Provides
    @Singleton
    fun provideApp(@ApplicationContext context: Context): App = context as App

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "happy-friend")
            .addCallback(PrepopulateMyWishlistFriend())
            .addCallback(PrepopulateGeneralIdeasList())
            .build()
    }

    @Provides
    fun provideIdeasDao(database: AppDatabase): IdeasDao {
        return database.ideasDao
    }

    @Provides
    fun provideFriendsDao(database: AppDatabase): FriendsDao {
        return database.friendsDao
    }

    @Provides
    @Singleton
    fun provideFriendsDataSource(
        contactsDataSource: ContactsDataSource,
    ): FriendsDataSource {
        return DemoFriendsDataSource(contactsDataSource)
    }

    @Provides
    @Singleton
    fun provideIdeasDataSource(assetManager: AssetManager): IdeasDataSource {
        return DemoIdeasDataSource(
            fromAssetsDataSource = FromAssetsDataSource(
                path = BuildConfig.ASSET_NAME_IDEAS,
                assets = assetManager,
            )
        )
    }

    @Provides
    fun provideBirthdayFormatter(): BirthdayFormatter =
        LocalizedBirthdayFormatter(Locale.getDefault())

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}