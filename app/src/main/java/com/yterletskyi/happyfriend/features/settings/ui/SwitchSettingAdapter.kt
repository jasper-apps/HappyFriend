package com.yterletskyi.happyfriend.features.settings.ui

import com.yterletskyi.happyfriend.common.list.AdapterDelegate
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.databinding.ItemSettingsSwitchBinding
import com.yterletskyi.happyfriend.features.settings.domain.SwitchModelItem

class SwitchSettingAdapter(
    private val onSwitchChanged: (Int, Boolean) -> Unit,
) : AdapterDelegate<ItemSettingsSwitchBinding>(
    ItemSettingsSwitchBinding::inflate
) {

    override fun onViewHolderCreated(viewHolder: Holder<ItemSettingsSwitchBinding>) {
        with(viewHolder) {
            binding.toggle.setOnCheckedChangeListener { _, isChecked ->
                onSwitchChanged(adapterPosition, isChecked)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: Holder<ItemSettingsSwitchBinding>, item: ModelItem) {
        item as SwitchModelItem

        with(viewHolder.binding) {
            text.text = item.text
            toggle.isChecked = item.enabled
        }
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean {
        return item is SwitchModelItem
    }

    override fun getViewType(): Int = SWITCH_SETTING_VIEW_TYPE

    companion object {
        private const val SWITCH_SETTING_VIEW_TYPE = 1
    }
}