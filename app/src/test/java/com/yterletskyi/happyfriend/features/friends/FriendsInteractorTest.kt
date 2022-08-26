package com.yterletskyi.happyfriend.features.friends

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.content.res.AppCompatResources
import com.yterletskyi.happyfriend.common.BirthdayFormatter
import com.yterletskyi.happyfriend.features.contacts.data.Contact
import com.yterletskyi.happyfriend.features.contacts.data.ContactsDataSource
import com.yterletskyi.happyfriend.features.friends.data.Friend
import com.yterletskyi.happyfriend.features.friends.data.FriendsDataSource
import com.yterletskyi.happyfriend.features.friends.data.GlobalFriends
import com.yterletskyi.happyfriend.features.friends.domain.FriendModelItem
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractorImpl
import com.yterletskyi.happyfriend.features.settings.domain.GeneralIdeasController
import com.yterletskyi.happyfriend.features.settings.domain.MyWishlistController
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import java.util.UUID
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FriendsInteractorTest {

    private val context: Context = mockk {
        every { getString(any()) } returns "mocked_string"
    }

    private val mockkFriendsFlow: MutableStateFlow<List<Friend>> = MutableStateFlow(emptyList())
    private val mockkMyWishlistFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val mockkGeneralIdeasFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val friendsDataSource: FriendsDataSource = mockk {
        every { friendsFlow } returns mockkFriendsFlow

        val addFriendSlot = slot<Friend>()
        coEvery {
            addFriend(capture(addFriendSlot))
        } answers {
            mockkFriendsFlow.value = listOf(addFriendSlot.captured)
        }

        val updateFriendSlotId = slot<String>()
        val updateFriendSlotPosition = slot<Long>()
        coEvery {
            updateFriend(capture(updateFriendSlotId), capture(updateFriendSlotPosition))
        } answers {
            val oldFriends = mockkFriendsFlow.value
            val oldFriend = oldFriends.firstOrNull { it.id == updateFriendSlotId.captured }
            val oldFriendIndex = oldFriends.indexOf(oldFriend)

            if (oldFriendIndex != -1) {
                val newFriend = oldFriend!!.copy(position = updateFriendSlotPosition.captured)
                val newFriends = oldFriends
                    .toMutableList()
                    .apply { set(oldFriendIndex, newFriend) }

                mockkFriendsFlow.value = newFriends
            }
        }

        val removeFriendSlot = slot<Long>()
        coEvery {
            removeFriend(capture(removeFriendSlot))
        } answers {
            val friends = mockkFriendsFlow.value
                .toMutableList()
                .apply { removeIf { it.contactId == removeFriendSlot.captured } }
            mockkFriendsFlow.value = friends
        }

        val isFriendSlot = slot<Long>()
        coEvery {
            isFriend(capture(isFriendSlot))
        } coAnswers {
            val friends = mockkFriendsFlow.value
            friends.any { it.contactId == isFriendSlot.captured }
        }
    }

    private val contactsDataSource: ContactsDataSource = mockk {
        every { contactsFlow } returns flowOf(
            listOf(TEST_CONTACT_1)
        )
    }

    private val birthdayFormatter: BirthdayFormatter = mockk {
        every { format(any()) } returns "somehow_formatted_birthday"
    }

    private val myWishlistController: MyWishlistController = mockk {
        every { wishlistFlow } returns mockkMyWishlistFlow
    }

    private val generalIdeasController: GeneralIdeasController = mockk {
        every { generalIdeasFlow } returns mockkGeneralIdeasFlow
    }

    private val interactor = FriendsInteractorImpl(
        context,
        friendsDataSource = friendsDataSource,
        contactsDataSource = contactsDataSource,
        birthdayFormatter = birthdayFormatter,
        myWishlistController = myWishlistController,
        generalIdeasController = generalIdeasController,
    )

    @Before
    fun mockParseDrawable() {
        mockkStatic(AppCompatResources::class)
        every { AppCompatResources.getDrawable(any(), any()) } returns ColorDrawable(Color.BLACK)
    }

    @Test
    fun `should emit friends at start`() = runBlocking {
        // GIVEN
        // WHEN
        val friends = interactor.friendsFlow.first()

        // THEN
        assertNotNull(friends)
    }

    @Test
    fun `should emit new friends after addFriend called`() = runBlocking {
        // GIVEN
        val testContact = TEST_CONTACT_1
        val testFriend = TEST_FRIEND_1

        val friendModelItem = FriendModelItem(
            id = testFriend.id,
            contactId = testFriend.contactId,
            image = mockk(),
            fullName = testContact.name,
            birthday = birthdayFormatter.format(testContact.birthday),
            position = 1,
        )

        // WHEN
        interactor.addFriend(friendModelItem)

        // THEN
        val friends = interactor.friendsFlow.first()
        assertNotNull(friends.find { it.id == friendModelItem.id })
    }

    @Test
    fun `should update position after updateFriend called`() = runBlocking {
        // GIVEN
        val testContact = TEST_CONTACT_1
        val testFriend = TEST_FRIEND_1

        val newPosition = 2L
        val friendModelItem = FriendModelItem(
            id = testFriend.id,
            contactId = testFriend.contactId,
            image = mockk(),
            fullName = testContact.name,
            birthday = birthdayFormatter.format(testContact.birthday),
            position = 1,
        )
        val newFriendModelItem = friendModelItem.copy(position = newPosition)

        // WHEN
        interactor.addFriend(friendModelItem)
        interactor.updateFriend(newFriendModelItem)

        // THEN
        val friends = interactor.friendsFlow.first()
        val updatedFriend = friends.find { it.id == friendModelItem.id }

        assertNotNull(updatedFriend)
        assertEquals(newPosition, updatedFriend!!.position)
    }

    @Test
    fun `should remove friend if removeFriend called`() = runBlocking {
        val testContact = TEST_CONTACT_1
        val testFriend = TEST_FRIEND_1

        val friendModelItem = FriendModelItem(
            id = testFriend.id,
            contactId = testFriend.contactId,
            image = mockk(),
            fullName = testContact.name,
            birthday = birthdayFormatter.format(testContact.birthday),
            position = 1,
        )

        // WHEN
        interactor.addFriend(friendModelItem)
        interactor.removeFriend(friendModelItem.contactId)

        // THEN
        val friends = interactor.friendsFlow.first()
        val removedFriend = friends.find { it.id == friendModelItem.id }

        assertNull(removedFriend)
    }

    @Test
    fun `test isFriend`() = runBlocking {
        val testContact = TEST_CONTACT_1
        val testFriend = TEST_FRIEND_1

        val friendModelItem = FriendModelItem(
            id = testFriend.id,
            contactId = testFriend.contactId,
            image = mockk(),
            fullName = testContact.name,
            birthday = birthdayFormatter.format(testContact.birthday),
            position = 1,
        )

        // WHEN
        interactor.addFriend(friendModelItem)

        // THEN
        val isFriend = interactor.isFriend(friendModelItem.contactId)
        assertTrue(isFriend)
    }

    @Test
    fun `test getFriend`() = runBlocking {
        val testContact = TEST_CONTACT_1
        val testFriend = TEST_FRIEND_1

        val friendModelItem = FriendModelItem(
            id = testFriend.id,
            contactId = testFriend.contactId,
            image = mockk(),
            fullName = testContact.name,
            birthday = birthdayFormatter.format(testContact.birthday),
            position = 1,
        )

        // WHEN
        interactor.addFriend(friendModelItem)

        // THEN
        val friend = interactor.getFriend(friendModelItem.id).first()

        assertNotNull(friend)
        assertEquals(friendModelItem.id, friend!!.id)
    }

    @Test
    fun `should return friends sorted by position`() = runBlocking {
        // GIVEN
        val testFriend = TEST_FRIEND_1
        val testContact = TEST_CONTACT_1
        val friendModelItem = FriendModelItem(
            id = testFriend.id,
            contactId = testFriend.contactId,
            image = mockk(),
            fullName = testContact.name,
            birthday = birthdayFormatter.format(testContact.birthday),
            position = 212,
        )
        val friendModelItem2 = friendModelItem.copy(
            id = UUID.randomUUID().toString(),
            contactId = System.currentTimeMillis(),
            position = 1
        )
        val friendModelItem3 = friendModelItem.copy(
            id = UUID.randomUUID().toString(),
            contactId = System.currentTimeMillis(),
            position = 16
        )

        // WHEN
        interactor.addFriend(friendModelItem)
        interactor.addFriend(friendModelItem2)
        interactor.addFriend(friendModelItem3)

        val friends = interactor.friendsFlow.first()

        // THEN
        val positions = friends.map { it.position }
        val isSorted = positions.zipWithNext { a, b -> b > a }.all { true }
        assertTrue(isSorted)
    }

    @Test
    fun `should return MyWishlist friend if enabled`() = runBlocking {
        // GIVEN
        mockkMyWishlistFlow.value = true
        mockkFriendsFlow.value = mockkFriendsFlow.value
            .toMutableList()
            .apply { add(GlobalFriends.MyWishlistFriend) }

        // WHEN
        val friends = interactor.friendsFlow.first()

        // THEN
        val wishlistFriend = friends.find { it.id == GlobalFriends.MyWishlistFriend.id }
        assertNotNull(wishlistFriend)
    }

    @Test
    fun `should return GeneralIdeas friend if enabled`() = runBlocking {
        // GIVEN
        mockkGeneralIdeasFlow.value = true
        mockkFriendsFlow.value = mockkFriendsFlow.value
            .toMutableList()
            .apply { add(GlobalFriends.GeneralIdeas) }

        // WHEN
        val friends = interactor.friendsFlow.first()

        // THEN
        val generalIdeasFriend = friends.find { it.id == GlobalFriends.GeneralIdeas.id }
        assertNotNull(generalIdeasFriend)
    }

    companion object {
        private val TEST_FRIEND_1 = Friend(
            id = UUID.randomUUID().toString(),
            contactId = 1,
            position = 0
        )
        private val TEST_CONTACT_1 = Contact(
            id = 1,
            name = "John Doe",
            image = null,
            birthday = null
        )
    }
}
