package com.yterletskyi.happyfriend.features.friends.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.yterletskyi.happyfriend.features.friends.domain.FriendModelItem
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.collections.set
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val interactor: FriendsInteractor
) : ViewModel() {

    private var removeFriendRequestMap: MutableMap<FriendModelItem, Job> = mutableMapOf()

    private val friends: StateFlow<List<FriendModelItem>> = interactor.friendsFlow
        .onEach { _friendsLiveData.value = it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _friendsLiveData: MutableLiveData<List<FriendModelItem>> = MutableLiveData()
    val friendsLiveData: LiveData<List<FriendModelItem>> = _friendsLiveData
        .map { items ->
            items.filter {
                val removePendingIds = removeFriendRequestMap.keys.map { it.id }
                it.id !in removePendingIds
            }
        }

    val showEmptyState: LiveData<Boolean> = friendsLiveData
        .map { it.isEmpty() }

    init {
        friends.launchIn(viewModelScope)
    }

    private fun removeFriend(friendModelItem: FriendModelItem) {
        viewModelScope.launch {
            interactor.removeFriend(friendModelItem.contactId)
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

    fun scheduleRemoveFriendAt(item: FriendModelItem) {
        _friendsLiveData.value = friends.value
            .toMutableList()
            .apply { remove(item) }

        removeFriendRequestMap[item] = viewModelScope.launch {
            delay(DELAY_BEFORE_FRIEND_REMOVE)
            removeFriend(item)
        }
    }

    fun cancelRemoveFriendRequest(item: FriendModelItem) {
        removeFriendRequestMap.remove(item)
            ?.also { it.cancel() }

        _friendsLiveData.value = friends.value
    }

    companion object {
        /**
         * Depending on the view implementation, the delay can vary.
         * Current implementation matches [com.google.android.material.snackbar.Snackbar.LENGTH_SHORT]
         */
        private const val DELAY_BEFORE_FRIEND_REMOVE = 1500L
    }
}
