package com.yterletskyi.happyfriend.features.settings.ui

import com.yterletskyi.happyfriend.common.list.AdapterDelegate
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.databinding.ItemSettingsTextBinding

class TextSettingAdapter : AdapterDelegate<ItemSettingsTextBinding>(
    ItemSettingsTextBinding::inflate
) {

    override fun onBindViewHolder(viewHolder: Holder<ItemSettingsTextBinding>, item: ModelItem) {
        TODO("Not yet implemented")
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getViewType(): Int {
        TODO("Not yet implemented")
    }
}
