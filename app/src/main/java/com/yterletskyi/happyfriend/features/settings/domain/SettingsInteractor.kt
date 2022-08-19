package com.yterletskyi.happyfriend.features.settings.domain

import android.content.Context
import com.yterletskyi.happyfriend.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SettingsInteractor(
    private val context: Context,
    private val myWishlistController: MyWishlistController,
    private val appVersionController: AppVersionController,
) {

    val items: Flow<List<SwitchModelItem>> = flowOf(
        listOf(
            SwitchModelItem(
                text = context.getString(R.string.title_my_wishlist_setting),
                enabled = myWishlistController.isMyWishlistEnabled()
            ),
        )
    )
}