package com.jasperapps.happyfriend.common.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.preference.PreferenceManager
import androidx.room.Room
import com.jasperapps.happyfriend.App
import com.jasperapps.happyfriend.common.BirthdayFormatter
import com.jasperapps.happyfriend.common.LocalizedBirthdayFormatter
import com.jasperapps.happyfriend.common.analytics.Analytics
import com.jasperapps.happyfriend.common.analytics.CompoundAnalytics
import com.jasperapps.happyfriend.common.analytics.ToLogAnalytics
import com.jasperapps.happyfriend.common.data.AppDatabase
import com.jasperapps.happyfriend.features.contacts.data.ContactsDataSource
import com.jasperapps.happyfriend.features.contacts.data.ContactsFromAssetsDataSource
import com.jasperapps.happyfriend.features.contacts.data.DemoContactsDataSource
import com.jasperapps.happyfriend.features.friends.data.DemoFriendsDataSource
import com.jasperapps.happyfriend.features.friends.data.FriendsDao
import com.jasperapps.happyfriend.features.friends.data.FriendsDataSource
import com.jasperapps.happyfriend.features.ideas.data.DemoIdeasDataSource
import com.jasperapps.happyfriend.features.ideas.data.IdeasDao
import com.jasperapps.happyfriend.features.ideas.data.IdeasDataSource
import com.jasperapps.happyfriend.features.ideas.data.IdeasFromAssetsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

typealias OurFirebaseAnalytics = com.jasperapps.happyfriend.common.analytics.FirebaseAnalytics
typealias TheirFirebaseAnalytics = com.google.firebase.analytics.FirebaseAnalytics

@Module
@InstallIn(SingletonComponent::class)
object CommonDi {

    @Provides
    @Singleton
    fun provideApp(@ApplicationContext context: Context): App = context as App

    @Provides
    fun provideAssetsManager(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

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
    fun provideContactsDataSource(@ApplicationContext context: Context): ContactsDataSource {
        return DemoContactsDataSource(
            ContactsFromAssetsDataSource(
                context = context,
                jsonFilename = "contacts.json",
            )
        )
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
            fromAssetsDataSource = IdeasFromAssetsDataSource(
                assetManager = assetManager,
                jsonFilename = "ideas.json",
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
    @Provides
    fun provideToLogAnalytics(): ToLogAnalytics {
        return ToLogAnalytics()
    }

    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): TheirFirebaseAnalytics {
        return TheirFirebaseAnalytics.getInstance(context)
    }

    @Provides
    fun provideAnalytics(
        toLogAnalytics: ToLogAnalytics,
        theirFirebaseAnalytics: TheirFirebaseAnalytics,
    ): Analytics {
        val ourAnalytics = OurFirebaseAnalytics(theirFirebaseAnalytics)
        return CompoundAnalytics(ourAnalytics, toLogAnalytics)
    }
}
