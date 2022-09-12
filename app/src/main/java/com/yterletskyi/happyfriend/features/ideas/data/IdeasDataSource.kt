package com.yterletskyi.happyfriend.features.ideas.data

import kotlinx.coroutines.flow.Flow

interface IdeasDataSource {
    fun getIdea(id: String): Flow<Idea>
    fun getIdeas(friendId: String): Flow<List<Idea>>
    suspend fun addIdea(idea: Idea)
    suspend fun updateIdea(id: String, text: String, done: Boolean, position: Long)
    suspend fun removeIdea(id: String)
}
