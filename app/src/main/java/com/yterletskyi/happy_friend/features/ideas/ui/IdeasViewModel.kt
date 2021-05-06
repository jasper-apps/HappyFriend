package com.yterletskyi.happy_friend.features.ideas.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.features.ideas.model.IdeasInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class IdeasViewModel @AssistedInject constructor(
    @Assisted private val friendId: Long,
    @Assisted private val interactor: IdeasInteractor
) : ViewModel() {

    @AssistedFactory
    interface IdeasViewModelAssistedFactory {
        fun create(friendId: Long, interactor: IdeasInteractor): IdeasViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: IdeasViewModelAssistedFactory,
            friendId: Long,
            interactor: IdeasInteractor
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(friendId, interactor) as T
            }
        }
    }

    val ideas: LiveData<List<ModelItem>> = interactor.getIdeas(friendId)
        .asLiveData()

}