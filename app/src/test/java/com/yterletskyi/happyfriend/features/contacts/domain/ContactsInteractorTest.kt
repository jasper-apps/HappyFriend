package com.yterletskyi.happyfriend.features.contacts.domain

import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.features.contacts.data.Contact
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.friends.data.Friend
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ContactsInteractorTest {

    private val testContactsFlow: MutableStateFlow<List<Contact>> =
        MutableStateFlow(TEST_CONTACTS_LIST)

    private val contactsDataSource: ContactsDataSource = mockk {
        every { contactsFlow } returns testContactsFlow

        val searchSlot = slot<String>()
        every {
            search(capture(searchSlot))
        } answers {
            testContactsFlow.value = TEST_CONTACTS_LIST
                .filter { it.name.contains(searchSlot.captured) }
        }
    }
    private val testFriendsFlow: MutableStateFlow<List<Friend>> = MutableStateFlow(emptyList())
    private val friendsDataSource: FriendsDataSource = mockk {
        every { friendsFlow } returns testFriendsFlow
    }
    private val birthdayFormatter: BirthdayFormatter = mockk {
        every { format(any()) } returns "test-birthday-string"
    }

    private val interactor: ContactsInteractor = ContactsInteractorImpl(
        contactsDataSource = contactsDataSource,
        friendsDataSource = friendsDataSource,
        birthdayFormatter = birthdayFormatter,
    )

    @Test
    fun `should emit contacts at start`() = runBlocking {
        // GIVEN
        // WHEN
        val contactsList = interactor.contactsFlow.first()

        // THEN
        assertNotNull(contactsList)
    }

    @Test
    fun `should emit contacts after search called`() = runBlocking {
        // GIVEN
        val testContact = TEST_CONTACT_2

        // WHEN
        interactor.search(testContact.name)

        // THEN
        val contactsList = interactor.contactsFlow.first()

        assertEquals(1, contactsList.size)
        assertEquals(testContact.id, contactsList[0].id)
    }

    companion object {
        val TEST_CONTACT_1 = Contact(1, "Ivan Franko", null, null)
        val TEST_CONTACT_2 = Contact(2, "Vasyl Bilko", null, null)
        val TEST_CONTACTS_LIST = listOf(TEST_CONTACT_1, TEST_CONTACT_2)
    }
}
