package com.yterletskyi.happyfriend.features.ideas.data

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DemoIdeasDataSource : IdeasDataSource {

    private val id = AtomicInteger(1)
    private val position = AtomicLong(1)

    override fun getIdea(id: String): Flow<Idea> = flowOf()

    override fun getIdeas(friendId: String): Flow<List<Idea>> = flowOf(
        listOf(
            Idea(
                id = id.getAndIncrement().toString(),
                text = "Earrings",
                done = true,
                friendId = friendId,
                createdAt = System.currentTimeMillis(),
                position = position.getAndIncrement(),
            ),
            Idea(
                id = id.getAndIncrement().toString(),
                text = "Everest Puzzles",
                done = false,
                friendId = friendId,
                createdAt = System.currentTimeMillis(),
                position = position.getAndIncrement(),
            ),
            Idea(
                id = id.getAndIncrement().toString(),
                text = "Blue T-Shirt",
                done = true,
                friendId = friendId,
                createdAt = System.currentTimeMillis(),
                position = position.getAndIncrement(),
            ),
            Idea(
                id = id.getAndIncrement().toString(),
                text = "Extreme Driving Certificate",
                done = false,
                friendId = friendId,
                createdAt = System.currentTimeMillis(),
                position = position.getAndIncrement(),
            ),
        )
    )

    override suspend fun addIdea(idea: Idea) {}

    override suspend fun updateIdea(id: String, text: String, done: Boolean, position: Long) {}

    override suspend fun removeIdea(id: String) {}
}
