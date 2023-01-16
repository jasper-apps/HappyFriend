package com.jasperapps.happyfriend.features.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jasperapps.happyfriend.common.list.ModelItem
import com.jasperapps.happyfriend.features.settings.domain.SettingsInteractor
import com.jasperapps.happyfriend.features.settings.domain.SettingsModelItem
import com.jasperapps.happyfriend.features.settings.domain.SwitchModelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val interactor: SettingsInteractor,
) : ViewModel() {

    val settingsItems: LiveData<List<ModelItem>> = interactor.items.asLiveData()

    init {
        interactor.initialize()
    }

    fun changeBooleanSetting(index: Int, value: Boolean) {
        val item = settingsItems.value?.get(index) ?: return
        val switchItem = item as? SettingsModelItem ?: return
        viewModelScope.launch {
            when (switchItem) {
                is SwitchModelItem.MyWhishlist -> interactor.enableMyWishlist(value)
                is SwitchModelItem.GeneralIdeas -> interactor.enableGeneralIdeas(value)
                else -> throw IllegalArgumentException("unsupported item type: $switchItem")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        interactor.destroy()
    }
}
