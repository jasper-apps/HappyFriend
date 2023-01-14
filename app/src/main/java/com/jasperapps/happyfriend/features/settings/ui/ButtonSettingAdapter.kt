package com.jasperapps.happyfriend.features.settings.ui

import com.jasperapps.happyfriend.common.list.AdapterDelegate
import com.jasperapps.happyfriend.common.list.ModelItem
import com.jasperapps.happyfriend.databinding.ItemSettingsButtonBinding
import com.jasperapps.happyfriend.features.settings.domain.ButtonModelItem
import com.jasperapps.happyfriend.features.settings.domain.VersionModelItem

class ButtonSettingAdapter(
    private val onClicked: (Int) -> Unit,
) : AdapterDelegate<ItemSettingsButtonBinding>(
    ItemSettingsButtonBinding::inflate
) {

    override fun onViewHolderCreated(viewHolder: Holder<ItemSettingsButtonBinding>) {
        with(viewHolder) {
            binding.root.setOnClickListener { _ ->
                onClicked(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: Holder<ItemSettingsButtonBinding>, item: ModelItem) {
        item as ButtonModelItem
        with(viewHolder.binding) {
            title.text = item.title
        }
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean {
        return item is ButtonModelItem
    }

    override fun getViewType(): Int = CHANGE_PIN_VALUE_TYPE

    companion object {
        private const val CHANGE_PIN_VALUE_TYPE = 3
    }
}
