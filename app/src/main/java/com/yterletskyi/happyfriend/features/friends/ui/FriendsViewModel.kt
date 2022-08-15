package com.yterletskyi.happyfriend.features.friends.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.yterletskyi.happyfriend.features.friends.domain.FriendModelItem
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val interactor: FriendsInteractor
) : ViewModel() {

    val friends: LiveData<List<FriendModelItem>> = interactor.friendsFlow.asLiveData()

    val showEmptyState: LiveData<Boolean> = friends.map { it.isEmpty() }

    fun removeFriend(index: Int) = viewModelScope.launch {
        val friend = friends.value?.get(index)
        friend?.let {
            interactor.removeFriend(it.contactId)
        }
    }

    fun onFriendsMoved(newOrderFriends: List<FriendModelItem>) {
        // todo: save with positions
    }
}
