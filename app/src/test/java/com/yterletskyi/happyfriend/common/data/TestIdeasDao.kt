package com.yterletskyi.happyfriend.common.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.yterletskyi.happyfriend.features.friends.data.Friend
import com.yterletskyi.happyfriend.features.friends.data.FriendsDao
import com.yterletskyi.happyfriend.features.ideas.data.Idea
import com.yterletskyi.happyfriend.features.ideas.data.IdeasDao
import java.util.UUID
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestIdeasDao : TestCase() {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var db: AppDatabase
    private lateinit var ideasDao: IdeasDao
    private lateinit var friendsDao: FriendsDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        ideasDao = db.ideasDao
        friendsDao = db.friendsDao
    }

    private suspend fun addIdeaWithFriend(contactId: Long): Idea {
        val calcFriendId = "frnd_$contactId"
        if (!friendsDao.isFriend(contactId)) {
            val f = Friend(
                id = calcFriendId,
                contactId = contactId,
                position = 0,
            )
            friendsDao.addFriend(f)
        }
        val i = Idea(
            id = UUID.randomUUID().toString(),
            text = "unit test idea $contactId",
            done = false,
            friendId = calcFriendId,
            createdAt = System.currentTimeMillis(),
            position = System.currentTimeMillis(),
        )
        ideasDao.addIdea(i)

        return i
    }

    private suspend fun getIdeas(friendId: String): List<Idea> = ideasDao
        .getIdeas(friendId = friendId)
        .first()

    @Test
    fun testGetIdeasForFriend() = runBlocking {
        val i1 = addIdeaWithFriend(contactId = 1)
        val i2 = addIdeaWithFriend(contactId = 1)
        addIdeaWithFriend(contactId = 2)

        val ideas = getIdeas(i1.friendId)
        assertEquals(listOf(i1, i2), ideas)
    }

    @Test
    fun testRemoveIdea() = runBlocking {
        val i = addIdeaWithFriend(contactId = 1)
        addIdeaWithFriend(contactId = 81)
        addIdeaWithFriend(contactId = 143)
        ideasDao.removeIdea(i.id)
        val ideas = getIdeas(friendId = i.friendId)
        assertEquals(0, ideas.size)
    }

    @Test
    fun testUpdateIdea() = runBlocking {
        val i = addIdeaWithFriend(contactId = 1)
        ideasDao.updateIdea(
            id = i.id,
            text = "updated idea #67",
            done = true,
            position = i.position,
        )
        val updated = getIdeas(friendId = i.friendId)
            .single()
        assertEquals(
            i.copy(
                text = "updated idea #67",
                done = true
            ),
            updated
        )
    }

    @After
    fun closeDb() {
        db.close()
    }
}
