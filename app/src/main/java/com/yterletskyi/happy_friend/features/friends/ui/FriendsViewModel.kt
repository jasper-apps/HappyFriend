package com.yterletskyi.happy_friend.features.friends.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yterletskyi.happy_friend.features.friends.data.Friend
import com.yterletskyi.happy_friend.features.friends.data.FriendsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    dataSource: FriendsDataSource
) : ViewModel() {

    val friends: LiveData<List<Friend>> = dataSource.getFriends()
        .asLiveData()
}