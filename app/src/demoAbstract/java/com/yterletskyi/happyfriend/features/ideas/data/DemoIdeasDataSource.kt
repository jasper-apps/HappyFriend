package com.yterletskyi.happyfriend.features.ideas.data

import com.yterletskyi.happyfriend.common.data.FromAssetsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DemoIdeasDataSource(
    private val fromAssetsDataSource: FromAssetsDataSource<Idea>,
) : IdeasDataSource {

    override fun getIdeas(friendId: String): Flow<List<Idea>> = flowOf(
        fromAssetsDataSource.retrieve()
    )

    override fun getIdea(id: String): Flow<Idea> = throw NotImplementedError("should not be used")

    override suspend fun addIdea(idea: Idea) {}

    override suspend fun updateIdea(id: String, text: String, done: Boolean, position: Long) {}

    override suspend fun removeIdea(id: String) {}
}