package com.yterletskyi.happyfriend.features.ideas.domain

import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.features.ideas.data.Idea
import com.yterletskyi.happyfriend.features.ideas.data.IdeasDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IdeasInteractor {
    fun getIdea(id: String): Flow<IdeaModelItem>
    fun getIdeas(friendId: String): Flow<List<ModelItem>>
    suspend fun addIdea(friendId: String, ideaModel: IdeaModelItem)
    suspend fun updateIdea(ideaModel: IdeaModelItem)
    suspend fun removeIdea(id: String)
}

class IdeasInteractorImpl @Inject constructor(
    private val ideasDataSource: IdeasDataSource
) : IdeasInteractor {

    override fun getIdeas(friendId: String): Flow<List<ModelItem>> =
        ideasDataSource.getIdeas(friendId)
            .map { ideas ->
                ideas.map {
                    IdeaModelItem(
                        id = it.id,
                        text = it.text,
                        done = it.done,
                        focused = false,
                        position = it.position,
                    )
                }
            }
            .map { models ->
                mutableListOf<ModelItem>().apply {
                    addAll(models)
                    if (models.isEmpty() || !models.last().isEmpty()) {
                        add(AddIdeaModelItem)
                    }
                }
            }

    override fun getIdea(id: String): Flow<IdeaModelItem> {
        return ideasDataSource.getIdea(id).map {
            IdeaModelItem(
                id = it.id,
                text = it.text,
                done = it.done,
                focused = false,
                position = it.position,
            )
        }
    }

    override suspend fun updateIdea(ideaModel: IdeaModelItem) {
        ideasDataSource.updateIdea(
            ideaModel.id,
            ideaModel.text.toString(),
            ideaModel.done,
            ideaModel.position,
        )
    }

    override suspend fun addIdea(friendId: String, ideaModel: IdeaModelItem) {
        val idea = Idea(
            id = ideaModel.id,
            text = ideaModel.text.toString(),
            done = ideaModel.done,
            friendId = friendId,
            createdAt = System.currentTimeMillis(),
            position = ideaModel.position,
        )
        ideasDataSource.addIdea(idea)
    }

    override suspend fun removeIdea(id: String) {
        ideasDataSource.removeIdea(id)
    }
}
