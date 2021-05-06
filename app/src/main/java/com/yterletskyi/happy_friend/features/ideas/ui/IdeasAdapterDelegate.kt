package com.yterletskyi.happy_friend.features.ideas.ui

import com.yterletskyi.happy_friend.common.list.AdapterDelegate
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.databinding.ItemIdeaBinding
import com.yterletskyi.happy_friend.features.ideas.model.IdeaModelItem

class IdeasAdapterDelegate : AdapterDelegate<ItemIdeaBinding>(
    ItemIdeaBinding::inflate
) {

    override fun onBindViewHolder(viewHolder: Holder<ItemIdeaBinding>, item: ModelItem) {
        item as IdeaModelItem

        with(viewHolder.binding) {
            input.setText(item.text)
            checkbox.isChecked = item.done
        }
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is IdeaModelItem

    override fun getViewType(): Int = 1
}