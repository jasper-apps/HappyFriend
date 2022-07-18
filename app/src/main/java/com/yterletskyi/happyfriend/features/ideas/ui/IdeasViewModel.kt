package com.yterletskyi.happyfriend.features.ideas.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yterletskyi.happyfriend.App
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.features.friends.domain.FriendsInteractor
import com.yterletskyi.happyfriend.features.ideas.domain.IdeaModelItem
import com.yterletskyi.happyfriend.features.ideas.domain.IdeasInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IdeasViewModel @AssistedInject constructor(
    @Assisted app: App,
    @Assisted private val friendId: String,
    @Assisted private val ideasInteractor: IdeasInteractor,
    @Assisted private val friendsInteractor: FriendsInteractor
) : AndroidViewModel(app) {

    @AssistedFactory
    interface IdeasViewModelAssistedFactory {
        fun create(
            app: App,
            friendId: String,
            ideasInteractor: IdeasInteractor,
            friendsInteractor: FriendsInteractor
        ): IdeasViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: IdeasViewModelAssistedFactory,
            app: App,
            friendId: String,
            ideasInteractor: IdeasInteractor,
            friendsInteractor: FriendsInteractor
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(
                    app,
                    friendId,
                    ideasInteractor,
                    friendsInteractor
                ) as T
            }
        }
    }

    val title: LiveData<String> = friendsInteractor.getFriend(friendId)
        .map {
            it?.fullName ?: app.getString(R.string.title_ideas)
        }
        .asLiveData()

    val ideas: StateFlow<List<ModelItem>> = ideasInteractor.getIdeas(friendId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val ideasLiveData: LiveData<List<ModelItem>> = ideas
        .distinctUntilChanged { old, new -> old == new }
        .map { items ->
            items.map { item ->
                if (item is IdeaModelItem && item.id == lastAddedIdea?.id) {
                    item.copy(focused = true)
                } else {
                    item
                }
            }
        }
        .asLiveData()

    private var lastAddedIdea: IdeaModelItem? = null

    fun addIdea(text: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = IdeaModelItem.withText(text)
            ideasInteractor.addIdea(friendId, idea)
            lastAddedIdea = idea
        }
    }

    fun updateIdea(index: Int, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = ideas.value.getOrNull(index)
            (idea as? IdeaModelItem)
                ?.takeIf { it.text != text }
                ?.let {
                    val newIdea = it.copy(text = text)
                    ideasInteractor.updateIdea(newIdea)
                }
        }
    }

    fun updateIdea(index: Int, done: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = ideas.value.getOrNull(index)
            (idea as? IdeaModelItem)
                ?.takeIf { it.done != done }
                ?.let {
                    val newIdea = it.copy(done = done)
                    ideasInteractor.updateIdea(newIdea)
                }
        }
    }

    fun removeIdea(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = ideas.value.getOrNull(index)
            (idea as? IdeaModelItem)?.let {
                ideasInteractor.removeIdea(it.id)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        lastAddedIdea = null
    }
}
