package com.yterletskyi.happy_friend.features.ideas.model

import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.features.ideas.data.Idea
import com.yterletskyi.happy_friend.features.ideas.data.IdeasDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

interface IdeasInteractor {
    fun getIdeas(friendId: Long): Flow<List<ModelItem>>
    suspend fun addIdea(friendId: Long, ideaModel: IdeaModelItem)
}

class IdeasInteractorImpl @Inject constructor(
    private val dataSource: IdeasDataSource
) : IdeasInteractor {

    override fun getIdeas(friendId: Long): Flow<List<ModelItem>> = dataSource.getIdeas(friendId)
        .map {
            it.map {
                IdeaModelItem(
                    id = it.id,
                    text = it.text,
                    done = it.done
                )
            } + listOf(AddIdeaModelItem)
        }

    override suspend fun addIdea(friendId: Long, ideaModel: IdeaModelItem) {
        val idea = Idea(
            id = UUID.randomUUID().toString(),
            text = ideaModel.text,
            done = ideaModel.done,
            friendId = friendId,
            createdAt = System.currentTimeMillis()
        )
        dataSource.addIdea(idea)
    }

}