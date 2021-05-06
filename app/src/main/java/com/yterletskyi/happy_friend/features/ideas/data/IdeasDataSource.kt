package com.yterletskyi.happy_friend.features.ideas.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface IdeasDataSource {
    fun getIdeas(friendId: Long): Flow<List<Idea>>
    suspend fun addIdea(idea: Idea)
}

class InMemoryIdeasDataSource : IdeasDataSource {

    private val flow: MutableStateFlow<List<Idea>> = MutableStateFlow(
        emptyList()
    )

    override fun getIdeas(friendId: Long): Flow<List<Idea>> = flow
        .map { it.filter { it.friendId == friendId } }
        .distinctUntilChanged()

    override suspend fun addIdea(idea: Idea) {
        val ideas = flow.value
        val newIdeas = ideas
            .toMutableList()
            .apply { add(idea) }
        flow.value = newIdeas
    }

}