package com.yterletskyi.happyfriend.features.settings.ui

import com.yterletskyi.happyfriend.common.list.AdapterDelegate
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.databinding.ItemSettingsSwitchBinding

class SwitchSettingAdapter : AdapterDelegate<ItemSettingsSwitchBinding>(
    ItemSettingsSwitchBinding::inflate
) {

    override fun onBindViewHolder(viewHolder: Holder<ItemSettingsSwitchBinding>, item: ModelItem) {
        TODO("Not yet implemented")
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getViewType(): Int {
        TODO("Not yet implemented")
    }
}