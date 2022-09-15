package com.jasperapps.happyfriend.features.settings.di

import android.content.Context
import android.content.SharedPreferences
import com.jasperapps.happyfriend.features.friends.data.FriendsDao
import com.jasperapps.happyfriend.features.settings.domain.AppVersionController
import com.jasperapps.happyfriend.features.settings.domain.GeneralIdeasController
import com.jasperapps.happyfriend.features.settings.domain.InternalGeneralIdeasController
import com.jasperapps.happyfriend.features.settings.domain.InternalMyWishlistController
import com.jasperapps.happyfriend.features.settings.domain.MyWishlistController
import com.jasperapps.happyfriend.features.settings.domain.RealAppVersionController
import com.jasperapps.happyfriend.features.settings.domain.SettingsInteractor
import com.jasperapps.happyfriend.features.settings.domain.SettingsInteractorImpl
import com.jasperapps.happyfriend.features.settings.domain.SharedPrefGeneralIdeasController
import com.jasperapps.happyfriend.features.settings.domain.SharedPrefsMyWishlistController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object SettingsDi {

    @Provides
    fun provideSettingsInteractor(
        @ApplicationContext context: Context,
        myWishlistController: InternalMyWishlistController,
        appVersionController: AppVersionController,
        generalIdeasController: InternalGeneralIdeasController,
        friendsDao: FriendsDao,
    ): SettingsInteractor {
        return SettingsInteractorImpl(context, myWishlistController, appVersionController, generalIdeasController, friendsDao)
    }

    @Provides
    fun provideMyWishlistControllerInternal(sharedPreferences: SharedPreferences): InternalMyWishlistController {
        return SharedPrefsMyWishlistController(sharedPreferences)
    }

    @Provides
    fun provideGlobalIdeasControllerInternal(sharedPreferences: SharedPreferences): InternalGeneralIdeasController {
        return SharedPrefGeneralIdeasController(sharedPreferences)
    }

    @Provides
    fun provideMyWishlistController(myWishlistController: InternalMyWishlistController): MyWishlistController {
        return myWishlistController
    }

    @Provides
    fun provideGeneralIdeasController(generalIdeasController: InternalGeneralIdeasController): GeneralIdeasController {
        return generalIdeasController
    }

    @Provides
    fun provideAppVersionController(): AppVersionController {
        return RealAppVersionController()
    }
}
