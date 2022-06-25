package com.yterletskyi.happyfriend.features.contacts.data

import kotlinx.coroutines.flow.Flow

interface ContactsDataSource {
    val contactsFlow: Flow<List<Contact>>
    fun search(query: String = "")
}
