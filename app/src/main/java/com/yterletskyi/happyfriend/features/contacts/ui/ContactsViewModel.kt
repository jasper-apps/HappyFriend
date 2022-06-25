package com.yterletskyi.happyfriend.features.contacts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yterletskyi.happyfriend.features.contacts.domain.ContactModelItem
import com.yterletskyi.happyfriend.features.contacts.domain.ContactsInteractor
import com.yterletskyi.happyfriend.features.friends.domain.FriendModelItem
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val interactor: ContactsInteractor,
    private val friendsInteractor: FriendsInteractor
) : ViewModel() {

    private val contactsFlow: StateFlow<List<ContactModelItem>> = interactor.contactsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val contacts: LiveData<List<ContactModelItem>> = contactsFlow.asLiveData()

    fun search(query: String) = interactor.search(query)

    fun toggleFriend(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val contact = contactsFlow.value[index]
            if (friendsInteractor.isFriend(contact.id)) {
                friendsInteractor.removeFriend(contact.id)
            } else {
                friendsInteractor.addFriend(
                    FriendModelItem(
                        id = UUID.randomUUID().toString(),
                        contactId = contact.id,
                        fullName = contact.fullName,
                        image = contact.image,
                        birthday = contact.birthday
                    )
                )
            }
        }
    }
}
