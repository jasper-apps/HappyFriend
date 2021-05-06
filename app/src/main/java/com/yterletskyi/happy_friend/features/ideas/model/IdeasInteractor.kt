package com.yterletskyi.happy_friend.features.ideas.model

import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.features.ideas.data.Idea
import com.yterletskyi.happy_friend.features.ideas.data.IdeasDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

interface IdeasInteractor {
    fun getIdea(id: String): Flow<IdeaModelItem>
    fun getIdeas(friendId: Long): Flow<List<ModelItem>>
    suspend fun addIdea(friendId: Long, ideaModel: IdeaModelItem)
    suspend fun updateIdea(ideaModel: IdeaModelItem)
    suspend fun removeIdea(id: String)
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

    override fun getIdea(id: String): Flow<IdeaModelItem> {
        return dataSource.getIdea(id).map {
            IdeaModelItem(
                id = it.id,
                text = it.text,
                done = it.done
            )
        }
    }

    override suspend fun updateIdea(ideaModel: IdeaModelItem) {
        dataSource.updateIdea(ideaModel.id, ideaModel.text, ideaModel.done)
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

    override suspend fun removeIdea(id: String) {
        dataSource.removeIdea(id)
    }

}