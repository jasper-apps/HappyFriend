package com.yterletskyi.happy_friend.features.friends.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yterletskyi.happy_friend.features.friends.domain.FriendModelItem
import com.yterletskyi.happy_friend.features.friends.domain.FriendsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val interactor: FriendsInteractor
) : ViewModel() {

    val friends: LiveData<List<FriendModelItem>> by lazy {
        interactor.getFriends().asLiveData()
    }

    fun search(query: String) = interactor.getFriends(query)

}