package com.jasperapps.happyfriend.features.contacts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jasperapps.happyfriend.common.analytics.Analytics
import com.jasperapps.happyfriend.features.contacts.domain.ContactModelItem
import com.jasperapps.happyfriend.features.contacts.domain.ContactsInteractor
import com.jasperapps.happyfriend.features.friends.domain.FriendModelItem
import com.jasperapps.happyfriend.features.friends.domain.FriendsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsInteractor: ContactsInteractor,
    private val friendsInteractor: FriendsInteractor,
    private val analytics: Analytics,
) : ViewModel() {

    private val contactsFlow: StateFlow<List<ContactModelItem>> = contactsInteractor.contactsFlow
        .onEach {
            val userProperty = Analytics.UserProperty.NumberOfContacts(it.size)
            analytics.setUserProperty(userProperty)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val contacts: LiveData<List<ContactModelItem>> = contactsFlow.asLiveData()

    fun onContactsPermissionGranted() {
        contactsInteractor.initialize()
        friendsInteractor.initialize()
    }

    fun search(query: String) = contactsInteractor.search(query)

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
                        birthday = contact.birthday,
                        position = System.currentTimeMillis(),
                    )
                )
            }
        }
    }

    override fun onCleared() {
        contactsInteractor.destroy()
    }
}
