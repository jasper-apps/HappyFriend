package com.jasperapps.happyfriend.features.contacts.data

import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeContactsDataSource : ContactsDataSource {

    private val list = listOf(
        Contact(
            id = 1,
            image = null,
            name = "User 1",
            birthday = LocalDate.of(1995, 6, 30)
        ),
        Contact(
            id = 2,
            image = null,
            name = "User 2",
            birthday = LocalDate.of(1992, 2, 6)
        ),
        Contact(
            id = 3,
            image = null,
            name = "User 3",
            birthday = LocalDate.of(1993, 1, 10)
        ),
        Contact(
            id = 4,
            image = null,
            name = "User 4",
            birthday = LocalDate.of(1999, 2, 9)
        )
    )

    private val _contactsFlow: MutableStateFlow<List<Contact>> = MutableStateFlow(list)
    override val contactsFlow: Flow<List<Contact>> = _contactsFlow

    override fun initialize() {}

    override fun search(query: String) {
        _contactsFlow.value = list
    }

    override fun destroy() {}
}
