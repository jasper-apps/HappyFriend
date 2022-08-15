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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val interactor: FriendsInteractor
) : ViewModel() {

    private var removeFriendRequestMap: MutableMap<Int, Job> = mutableMapOf()

    val friends: LiveData<List<FriendModelItem>> = interactor.friendsFlow.asLiveData()

    val showEmptyState: LiveData<Boolean> = friends.map { it.isEmpty() }

    private fun removeFriend(index: Int) = viewModelScope.launch {
        val friend = friends.value?.get(index)
        friend?.let {
            interactor.removeFriend(it.contactId)
        }
    }

    fun onFriendsMoved(newOrderFriends: List<FriendModelItem>) {
        viewModelScope.launch {
            newOrderFriends.forEachIndexed { i, it ->
                val newFriend = it.copy(position = i.toLong())
                interactor.updateFriend(newFriend)
            }
        }
    }

    fun scheduleRemoveFriendAt(index: Int) {
        val removeJob = viewModelScope.launch {
            delay(DELAY_BEFORE_FRIEND_REMOVE)
            removeFriend(index)
        }
        removeFriendRequestMap[index] = removeJob
    }

    fun cancelRemoveFriendRequest(index: Int) {
        removeFriendRequestMap[index]?.cancel()
    }

    companion object {
        /**
         * Depending on the view implementation, the delay can vary.
         * Current implementation matches [com.google.android.material.snackbar.Snackbar.LENGTH_SHORT]
         */
        private const val DELAY_BEFORE_FRIEND_REMOVE = 1500L
    }
}
