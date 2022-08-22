package com.yterletskyi.happyfriend.features.settings.domain

import com.yterletskyi.happyfriend.common.list.ModelItem

data class SwitchModelItem(
    val text: String,
    val enabled: Boolean,
    val type: SettingEnum,
) : ModelItem
