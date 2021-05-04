package com.yterletskyi.happy_friend.features.contacts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yterletskyi.happy_friend.features.contacts.domain.ContactModelItem
import com.yterletskyi.happy_friend.features.contacts.domain.ContactsInteractor
import com.yterletskyi.happy_friend.features.friends.domain.FriendModelItem
import com.yterletskyi.happy_friend.features.friends.domain.FriendsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val interactor: ContactsInteractor,
    private val friendsInteractor: FriendsInteractor
) : ViewModel() {

    private val contactsFlow: StateFlow<List<ContactModelItem>> = interactor.getContacts()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val contacts: LiveData<List<ContactModelItem>> = contactsFlow.asLiveData()

    fun search(query: String) = interactor.getContacts(query)

    fun favoriteContact(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val contact = contactsFlow.value[index]
            val friend = FriendModelItem(
                id = contact.id,
                fullName = contact.fullName,
                image = contact.image,
                birthday = contact.birthday
            )
            friendsInteractor.addFriend(friend)
        }
    }

}