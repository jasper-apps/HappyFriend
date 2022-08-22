package com.yterletskyi.happyfriend.features.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.features.settings.domain.SettingEnum
import com.yterletskyi.happyfriend.features.settings.domain.SettingsInteractor
import com.yterletskyi.happyfriend.features.settings.domain.SwitchModelItem
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
        item as SwitchModelItem
        viewModelScope.launch {
            when (item.type) {
                SettingEnum.MY_WISHLIST -> interactor.enableMyWishlist(value)
                else -> {}
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        interactor.destroy()
    }
}
