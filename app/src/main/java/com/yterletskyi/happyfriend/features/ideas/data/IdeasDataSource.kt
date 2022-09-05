package com.yterletskyi.happyfriend.features.ideas.data

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

interface IdeasDataSource {
    fun getIdea(id: String): Flow<Idea>
    fun getIdeas(friendId: String): Flow<List<Idea>>
    suspend fun addIdea(idea: Idea)
    suspend fun updateIdea(id: String, text: String, done: Boolean, position: Long)
    suspend fun removeIdea(id: String)
}


