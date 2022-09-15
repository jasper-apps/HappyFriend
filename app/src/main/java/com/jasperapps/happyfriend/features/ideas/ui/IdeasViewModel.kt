package com.jasperapps.happyfriend.features.ideas.ui

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jasperapps.happyfriend.App
import com.jasperapps.happyfriend.R
import com.jasperapps.happyfriend.common.list.ModelItem
import com.jasperapps.happyfriend.features.friends.domain.FriendsInteractor
import com.jasperapps.happyfriend.features.ideas.domain.IdeaModelItem
import com.jasperapps.happyfriend.features.ideas.domain.IdeasInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class IdeasViewModel @Inject constructor(
    app: App,
    private val handle: SavedStateHandle,
    private val ideasInteractor: IdeasInteractor,
    private val friendsInteractor: FriendsInteractor
) : AndroidViewModel(app) {

    private val friendId: String = handle["friendId"]
        ?: error("friendId is not passed")

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

    fun onIdeasMoved(newOrderIdeas: List<IdeaModelItem>) {
        viewModelScope.launch {
            newOrderIdeas.forEachIndexed { i, it ->
                val newIdea = it.copy(position = i.toLong())
                ideasInteractor.updateIdea(newIdea)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        lastAddedIdea = null
    }
}
