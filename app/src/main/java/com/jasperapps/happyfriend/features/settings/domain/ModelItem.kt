package com.jasperapps.happyfriend.features.settings.domain

import com.jasperapps.happyfriend.common.list.ModelItem

sealed interface SettingsModelItem : ModelItem

sealed class SwitchModelItem(
    open val text: String,
    open val enabled: Boolean,
) : SettingsModelItem {

    class MyWhishlist(
        override val text: String,
        override val enabled: Boolean,
    ) : SwitchModelItem(text, enabled)

    class GeneralIdeas(
        override val text: String,
        override val enabled: Boolean,
    ) : SwitchModelItem(text, enabled)
}

data class VersionModelItem(
    val title: String,
    val appVersion: String,
) : SettingsModelItem

data class ButtonModelItem(
    val title: String,
) : SettingsModelItem