package com.jasperapps.happyfriend.features.settings.domain

import com.jasperapps.happyfriend.common.list.ModelItem

data class SwitchModelItem(
    val text: String,
    val enabled: Boolean,
    val type: SettingEnum,
) : ModelItem

data class VersionModelItem(
    val title: String,
    val appVersion: String,
    val type: SettingEnum
) : ModelItem
