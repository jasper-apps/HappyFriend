package com.yterletskyi.happy_friend.features.friends.domain

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import com.yterletskyi.happy_friend.common.drawable.AvatarDrawable
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import com.yterletskyi.happy_friend.features.friends.data.initials
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FriendsInteractor {
    fun getFriends(query: String = ""): Flow<List<FriendModelItem>>
}

class FriendsInteractorImpl @Inject constructor(
    private val context: Context,
    private val dataSource: FriendsDataSource
) : FriendsInteractor {

    override fun getFriends(query: String): Flow<List<FriendModelItem>> = dataSource.getFriends(query)
        .map {
            it.map {
                FriendModelItem(
                    id = it.id,
                    image = it.imageUri
                        ?.let { drawableFromUri(it) }
                        ?: AvatarDrawable(it.initials),
                    fullName = it.name,
                    birthday = it.birthday
                )
            }
        }

    private fun drawableFromUri(uri: Uri): Drawable = context.contentResolver.openInputStream(uri)
        .use { Drawable.createFromStream(it, uri.toString()) }

}