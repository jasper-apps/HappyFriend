package com.yterletskyi.happy_friend.features.ideas.ui

import androidx.lifecycle.*
import com.yterletskyi.happy_friend.App
import com.yterletskyi.happy_friend.R
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.features.friends.domain.FriendsInteractor
import com.yterletskyi.happy_friend.features.ideas.domain.IdeaModelItem
import com.yterletskyi.happy_friend.features.ideas.domain.IdeasInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class IdeasViewModel @AssistedInject constructor(
    @Assisted app: App,
    @Assisted private val friendId: String,
    @Assisted private val ideasInteractor: IdeasInteractor,
    @Assisted private val friendsInteractor: FriendsInteractor,
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
        .asLiveData()

    fun addIdea() {
        viewModelScope.launch(Dispatchers.IO) {
            val idea = IdeaModelItem.empty()
            ideasInteractor.addIdea(friendId, idea)
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

}