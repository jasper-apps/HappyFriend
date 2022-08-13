package com.yterletskyi.happyfriend.features.ideas.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface IdeasDataSource {
    fun getIdea(id: String): Flow<Idea>
    fun getIdeas(friendId: String): Flow<List<Idea>>
    suspend fun addIdea(idea: Idea)
    suspend fun updateIdea(id: String, text: String, done: Boolean, position: String)
    suspend fun removeIdea(id: String)
}

class InMemoryIdeasDataSource : IdeasDataSource {

    private val flow: MutableStateFlow<List<Idea>> = MutableStateFlow(
        emptyList()
    )

    override fun getIdea(id: String): Flow<Idea> = flow
        .map { it.single { it.id == id } }

    override fun getIdeas(friendId: String): Flow<List<Idea>> = flow
        .map { it.filter { it.friendId == friendId } }
        .distinctUntilChanged()

    override suspend fun addIdea(idea: Idea) {
        val ideas = flow.value
        val newIdeas = ideas
            .toMutableList()
            .apply { add(idea) }
        flow.value = newIdeas
    }

    override suspend fun updateIdea(id: String, text: String, done: Boolean, position: String) {
        val ideas = flow.value
        val newIdeas = ideas
            .toMutableList()
            .apply {
                val index = indexOfFirst { it.id == id }
                val oldIdea = get(index)
                val newIdea = oldIdea.copy(
                    text = text,
                    done = done,
                    position = position,
                )
                set(index, newIdea)
            }
        flow.value = newIdeas
    }

    override suspend fun removeIdea(id: String) {
        val ideas = flow.value
        val newIdeas = ideas
            .toMutableList()
            .apply {
                removeIf { it.id == id }
            }
        flow.value = newIdeas
    }
}

class RoomIdeasDataSource(
    private val ideasDao: IdeasDao
) : IdeasDataSource {

    override fun getIdea(id: String): Flow<Idea> = ideasDao.getIdea(id)

    override fun getIdeas(friendId: String): Flow<List<Idea>> = ideasDao.getIdeas(friendId)

    override suspend fun addIdea(idea: Idea) = ideasDao.addIdea(idea)

    override suspend fun updateIdea(id: String, text: String, done: Boolean, position: String) =
        ideasDao.updateIdea(id, text, done, position)

    override suspend fun removeIdea(id: String) = ideasDao.removeIdea(id)
}
