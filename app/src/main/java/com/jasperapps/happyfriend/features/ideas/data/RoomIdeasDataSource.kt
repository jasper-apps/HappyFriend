package com.jasperapps.happyfriend.features.ideas.data

import kotlinx.coroutines.flow.Flow

class RoomIdeasDataSource(
    private val ideasDao: IdeasDao
) : IdeasDataSource {

    override fun getIdea(id: String): Flow<Idea> = ideasDao.getIdea(id)

    override fun getIdeas(friendId: String): Flow<List<Idea>> = ideasDao.getIdeas(friendId)

    override suspend fun addIdea(idea: Idea) = ideasDao.addIdea(idea)

    override suspend fun updateIdea(id: String, text: String, done: Boolean, position: Long) =
        ideasDao.updateIdea(id, text, done, position)

    override suspend fun removeIdea(id: String) = ideasDao.removeIdea(id)
}
