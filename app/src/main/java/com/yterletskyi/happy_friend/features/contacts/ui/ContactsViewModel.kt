package com.yterletskyi.happy_friend.features.contacts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yterletskyi.happy_friend.features.contacts.domain.ContactModelItem
import com.yterletskyi.happy_friend.features.contacts.domain.ContactsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val interactor: ContactsInteractor
) : ViewModel() {

    val contacts: LiveData<List<ContactModelItem>> by lazy {
        interactor.getContacts().asLiveData()
    }

    fun search(query: String) = interactor.getContacts(query)

}