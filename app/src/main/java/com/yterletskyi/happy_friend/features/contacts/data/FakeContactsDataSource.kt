package com.yterletskyi.happy_friend.features.contacts.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class FakeContactsDataSource : ContactsDataSource {

    private val list = listOf(
        Contact(
            id = 1,
            imageUri = null,
            name = "Yura Basa",
            birthday = LocalDate.of(1995, 6, 30)
        ),
        Contact(
            id = 2,
            imageUri = null,
            name = "Ostap Holub",
            birthday = LocalDate.of(1997, 2, 6)
        ),
        Contact(
            id = 3,
            imageUri = null,
            name = "Andre Yaniv",
            birthday = LocalDate.of(1997, 1, 10)
        ),
        Contact(
            id = 4,
            imageUri = null,
            name = "Taras Smakula",
            birthday = LocalDate.of(1997, 2, 9)
        )
    )

    private val _contactsFlow: MutableStateFlow<List<Contact>> = MutableStateFlow(list)
    override val contactsFlow: Flow<List<Contact>> = _contactsFlow

    override fun search(query: String) {
        _contactsFlow.value = list
    }
}