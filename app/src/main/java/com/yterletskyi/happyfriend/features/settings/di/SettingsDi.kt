package com.yterletskyi.happyfriend.features.settings.di

import android.content.Context
import android.content.SharedPreferences
import com.yterletskyi.happyfriend.features.friends.data.FriendsDao
import com.yterletskyi.happyfriend.features.settings.domain.AppVersionController
import com.yterletskyi.happyfriend.features.settings.domain.GeneralIdeaController
import com.yterletskyi.happyfriend.features.settings.domain.InternalGeneralIdeaController
import com.yterletskyi.happyfriend.features.settings.domain.InternalMyWishlistController
import com.yterletskyi.happyfriend.features.settings.domain.MyWishlistController
import com.yterletskyi.happyfriend.features.settings.domain.RealAppVersionController
import com.yterletskyi.happyfriend.features.settings.domain.SettingsInteractor
import com.yterletskyi.happyfriend.features.settings.domain.SettingsInteractorImpl
import com.yterletskyi.happyfriend.features.settings.domain.SharedPrefGeneralIdeaController
import com.yterletskyi.happyfriend.features.settings.domain.SharedPrefsMyWishlistController
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
        generalIdeaController: InternalGeneralIdeaController,
        friendsDao: FriendsDao,
    ): SettingsInteractor {
        return SettingsInteractorImpl(context, myWishlistController, appVersionController, generalIdeaController ,friendsDao)
    }

    @Provides
    fun provideMyWishlistControllerInternal(sharedPreferences: SharedPreferences): InternalMyWishlistController {
        return SharedPrefsMyWishlistController(sharedPreferences)
    }

    @Provides
    fun provideGlobalIdeasControllerInternal(sharedPreferences: SharedPreferences) : InternalGeneralIdeaController {
        return SharedPrefGeneralIdeaController(sharedPreferences)
    }

    @Provides
    fun provideMyWishlistController(myWishlistController: InternalMyWishlistController): MyWishlistController {
        return myWishlistController
    }

    @Provides
    fun provideGeneralIdeasController(generalIdeaController: InternalGeneralIdeaController) : GeneralIdeaController {
        return generalIdeaController
    }

    @Provides
    fun provideAppVersionController(): AppVersionController {
        return RealAppVersionController()
    }
}
