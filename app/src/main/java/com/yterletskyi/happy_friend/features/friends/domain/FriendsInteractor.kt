package com.yterletskyi.happy_friend.features.friends.domain

import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FriendsInteractor {
    fun getFriends(): Flow<List<FriendModelItem>>
}

class FriendsInteractorImpl @Inject constructor(
    private val dataSource: FriendsDataSource
) : FriendsInteractor {

    override fun getFriends(): Flow<List<FriendModelItem>> = dataSource.getFriends()
        .map {
            it.map {
                FriendModelItem(
                    id = it.id,
                    fullName = "${it.firstName} ${it.lastName}",
                    birthday = it.birthday
                )
            }
        }

}