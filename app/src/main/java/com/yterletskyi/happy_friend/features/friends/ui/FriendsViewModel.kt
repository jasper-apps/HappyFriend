package com.yterletskyi.happy_friend.features.friends.ui

import androidx.lifecycle.*
import com.yterletskyi.happy_friend.features.friends.domain.FriendModelItem
import com.yterletskyi.happy_friend.features.friends.domain.FriendsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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

}
