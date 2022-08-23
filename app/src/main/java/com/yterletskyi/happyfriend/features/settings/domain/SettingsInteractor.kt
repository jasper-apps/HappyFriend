package com.yterletskyi.happyfriend.features.settings.domain

import android.content.Context
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.LifecycleComponent
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.features.friends.data.FriendsDao
import com.yterletskyi.happyfriend.features.friends.data.GlobalFriends
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SettingsInteractor : LifecycleComponent {
    val items: Flow<List<ModelItem>>
    suspend fun enableMyWishlist(enable: Boolean)
}

class SettingsInteractorImpl @Inject constructor(
    private val context: Context,
    private val myWishlistController: InternalMyWishlistController,
    private val appVersionController: AppVersionController,
    private val friendsDao: FriendsDao,
) : SettingsInteractor {

    private val myWishlistFlow = myWishlistController.wishlistFlow

    override val items: Flow<List<ModelItem>> = myWishlistFlow.map {
        listOf(
            SwitchModelItem(
                text = context.getString(R.string.title_my_wishlist_setting),
                enabled = it,
                type = SettingEnum.MY_WISHLIST,
            ),
            VersionModelItem(
                title = context.getString(R.string.app_version_title),
                appVersionController.getAppVersion(),
                SettingEnum.APP_VERSION,
            )
        )
    }

    override fun initialize() {
        myWishlistController.initialize()
    }

    override suspend fun enableMyWishlist(enable: Boolean) {
        if (!enable) {
            friendsDao.updateFriend(
                GlobalFriends.MyWishlistFriend.id,
                GlobalFriends.MyWishlistFriend.position
            )
        }
        myWishlistController.setMyWishListEnabled(enable)
    }

    override fun destroy() {
        myWishlistController.destroy()
    }
}
