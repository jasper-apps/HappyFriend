package com.yterletskyi.happyfriend.features.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yterletskyi.happyfriend.features.settings.domain.SettingsInteractor
import com.yterletskyi.happyfriend.features.settings.domain.SwitchModelItem

class SettingsViewModel(
    interactor: SettingsInteractor,
) : ViewModel() {

    val settingsItems: LiveData<List<SwitchModelItem>> = interactor.items
        .asLiveData()
}
