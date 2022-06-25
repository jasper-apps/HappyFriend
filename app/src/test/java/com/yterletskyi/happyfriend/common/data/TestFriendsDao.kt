package com.yterletskyi.happyfriend.common.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.yterletskyi.happyfriend.features.friends.data.Friend
import com.yterletskyi.happyfriend.features.friends.data.FriendsDao
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestFriendsDao : TestCase() {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var db: AppDatabase
    private lateinit var friendsDao: FriendsDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        friendsDao = db.friendsDao
    }

    private suspend fun addFriend(): Friend = Friend(
        id = "f1",
        contactId = 1
    ).also {
        friendsDao.addFriend(it)
    }

    private suspend fun getFriends(): List<Friend> = friendsDao
        .getFriends()
        .first()

    @Test
    fun testAddFriend() = runBlocking {
        val f = addFriend()
        val friends = getFriends()

        assertEquals(1, friends.size)
        assertEquals(f, friends[0])
    }

    @Test
    fun testRemoveFriend() = runBlocking {
        val f = addFriend()
        friendsDao.removeFriend(f.contactId)

        val friends = getFriends()
        assertEquals(0, friends.size)
    }

    @Test
    fun testIsFriend() = runBlocking {
        val f = addFriend()
        val isFriend = friendsDao.isFriend(f.contactId)

        assertTrue(isFriend)
    }

    @After
    fun closeDb() {
        db.close()
    }
}
