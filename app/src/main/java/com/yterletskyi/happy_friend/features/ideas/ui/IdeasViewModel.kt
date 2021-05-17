package com.yterletskyi.happy_friend.features.ideas.ui

import androidx.lifecycle.*
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.features.ideas.domain.IdeaModelItem
import com.yterletskyi.happy_friend.features.ideas.domain.IdeasInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IdeasViewModel @AssistedInject constructor(
    @Assisted private val friendId: String,
    @Assisted private val interactor: IdeasInteractor
) : ViewModel() {

    @AssistedFactory
    interface IdeasViewModelAssistedFactory {
        fun create(friendId: String, interactor: IdeasInteractor): IdeasViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: IdeasViewModelAssistedFactory,
            friendId: String,
            interactor: IdeasInteractor
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(friendId, interactor) as T
            }
        }
    }

    val ideas: StateFlow<List<ModelItem>> = interactor.getIdeas(friendId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val ideasLiveData: LiveData<List<ModelItem>> = ideas.asLiveData()

    fun addIdea() {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = IdeaModelItem.empty()
            interactor.addIdea(friendId, idea)
        }
    }

    fun updateIdea(index: Int, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = ideas.value.getOrNull(index)
            (idea as? IdeaModelItem)?.let {
                val newIdea = it.copy(text = text)
                interactor.updateIdea(newIdea)
            }
        }
    }

    fun updateIdea(index: Int, done: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = ideas.value.getOrNull(index)
            (idea as? IdeaModelItem)?.let {
                val newIdea = it.copy(done = done)
                interactor.updateIdea(newIdea)
            }
        }
    }

    fun removeIdea(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = ideas.value.getOrNull(index)
            (idea as? IdeaModelItem)?.let {
                interactor.removeIdea(it.id)
            }
        }
    }

}