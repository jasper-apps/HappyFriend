package com.yterletskyi.happyfriend.features.settings.domain

import android.content.Context
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.LifecycleComponent
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SettingsInteractor : LifecycleComponent {
    val items: Flow<List<SwitchModelItem>>
    fun enableMyWishlist(enable: Boolean)
}

class SettingsInteractorImpl @Inject constructor(
    private val context: Context,
    private val myWishlistController: InternalMyWishlistController,
    private val appVersionController: AppVersionController,
) : SettingsInteractor {

    private val myWishlistFlow = myWishlistController.wishlistFlow

    override val items: Flow<List<SwitchModelItem>> = myWishlistFlow.map {
        listOf(
            SwitchModelItem(
                text = context.getString(R.string.title_my_wishlist_setting),
                enabled = it,
                type = SettingEnum.MY_WISHLIST,
            ),
        )
    }

    override fun initialize() {
        myWishlistController.initialize()
    }

    override fun enableMyWishlist(enable: Boolean) {
        myWishlistController.setMyWishListEnabled(enable)
    }

    override fun destroy() {
        myWishlistController.destroy()
    }
}