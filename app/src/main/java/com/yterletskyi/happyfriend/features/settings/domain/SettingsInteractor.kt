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
    suspend fun enableMyGlobalIdealList(enable: Boolean)
}

class SettingsInteractorImpl @Inject constructor(
    private val context: Context,
    private val myWishlistController: InternalMyWishlistController,
    private val appVersionController: AppVersionController,
    private val generalIdeaController: InternalGeneralIdeaController,
    private val friendsDao: FriendsDao,
) : SettingsInteractor {

    private val myWishlistFlow = myWishlistController.wishlistFlow
    private val generalIdeaFlow = generalIdeaController.generalidealist

    override val items: Flow<List<ModelItem>> = combine(myWishlistFlow, generalIdeaFlow) { myWishListEnabled, GeneralIdeaEnanled ->
        listOf(
            SwitchModelItem(
                text = context.getString(R.string.title_my_wishlist_setting),
                enabled = myWishListEnabled,
                type = SettingEnum.MY_WISHLIST,
            ),
            SwitchModelItem(
                text = context.getString(R.string.title_my_general_ideas),
                enabled = GeneralIdeaEnanled,
                type = SettingEnum.GENERAL_LIST,
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
        generalIdeaController.initialize()
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

    override suspend fun enableMyGlobalIdealList(enable: Boolean) {
        if(!enable) {
            friendsDao.updateFriend(
                GlobalFriends.MyGlobalIdea.id,
                GlobalFriends.MyGlobalIdea.position
            )
        }
        generalIdeaController.setListEnabled(enable)
    }

    override fun destroy() {
        myWishlistController.destroy()
        generalIdeaController.destroy()
    }
}
