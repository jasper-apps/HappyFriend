package com.jasperapps.happyfriend.features.ideas.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IdeasDao {

    @Query("SELECT * FROM idea WHERE id = :id LIMIT 1")
    fun getIdea(id: String): Flow<Idea>

    @Query("SELECT * FROM idea WHERE friend_id = :friendId ORDER BY position ASC")
    fun getIdeas(friendId: String): Flow<List<Idea>>

    @Insert
    suspend fun addIdea(idea: Idea)

    @Query("UPDATE idea SET text = :text, done = :done, position = :position WHERE id = :id")
    suspend fun updateIdea(id: String, text: String, done: Boolean, position: Long)

    @Query("DELETE from idea WHERE id = :id")
    suspend fun removeIdea(id: String)
}
