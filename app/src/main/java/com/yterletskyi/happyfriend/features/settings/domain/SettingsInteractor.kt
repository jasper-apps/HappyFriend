package com.yterletskyi.happyfriend.features.settings.domain

import android.content.Context
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.LifecycleComponent
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.features.friends.data.FriendsDao
import com.yterletskyi.happyfriend.features.friends.data.GlobalFriends
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface SettingsInteractor : LifecycleComponent {
    val items: Flow<List<ModelItem>>
    suspend fun enableMyWishlist(enable: Boolean)
    suspend fun enableGeneralIdeas(enable: Boolean)
}

class SettingsInteractorImpl @Inject constructor(
    private val context: Context,
    private val myWishlistController: InternalMyWishlistController,
    private val appVersionController: AppVersionController,
    private val generalIdeasController: InternalGeneralIdeasController,
    private val friendsDao: FriendsDao,
) : SettingsInteractor {

    private val myWishlistFlow = myWishlistController.wishlistFlow
    private val generalIdeasFlow = generalIdeasController.generalIdeasFlow

    override val items: Flow<List<ModelItem>> =
        combine(myWishlistFlow, generalIdeasFlow) { myWishListEnabled, generalIdeaEnabled ->
            listOf(
                SwitchModelItem(
                    text = context.getString(R.string.title_my_wishlist_setting),
                    enabled = myWishListEnabled,
                    type = SettingEnum.MY_WISHLIST,
                ),
                SwitchModelItem(
                    text = context.getString(R.string.title_my_general_ideas),
                    enabled = generalIdeaEnabled,
                    type = SettingEnum.GENERAL_IDEAS,
                ),
                VersionModelItem(
                    title = context.getString(R.string.app_version_title),
                    appVersion = appVersionController.getAppVersion(),
                    type = SettingEnum.APP_VERSION,
                )
            )
        }

    override fun initialize() {
        myWishlistController.initialize()
        generalIdeasController.initialize()
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

    override suspend fun enableGeneralIdeas(enable: Boolean) {
        if (!enable) {
            friendsDao.updateFriend(
                GlobalFriends.GeneralIdeas.id,
                GlobalFriends.GeneralIdeas.position
            )
        }
        generalIdeasController.setGeneralIdeasEnabled(enable)
    }

    override fun destroy() {
        myWishlistController.destroy()
        generalIdeasController.destroy()
    }
}
