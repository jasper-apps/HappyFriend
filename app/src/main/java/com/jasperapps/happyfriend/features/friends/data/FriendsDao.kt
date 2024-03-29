package com.jasperapps.happyfriend.features.friends.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {

    @Query("SELECT * FROM friend ORDER BY position ASC")
    fun getFriends(): Flow<List<Friend>>

    @Insert
    suspend fun addFriend(friend: Friend)

    @Query("DELETE FROM friend WHERE contact_id =:contactId")
    suspend fun removeFriend(contactId: Long)

    @Query("SELECT EXISTS (SELECT 1 FROM friend WHERE contact_id = :contactId) LIMIT 1")
    suspend fun isFriend(contactId: Long): Boolean

    @Query("UPDATE friend SET position = :position WHERE id = :id")
    suspend fun updateFriend(id: String, position: Long)
}
