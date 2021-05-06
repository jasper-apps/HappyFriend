package com.yterletskyi.happy_friend.features.ideas.ui

import com.yterletskyi.happy_friend.common.list.AdapterDelegate
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.databinding.ItemAddIdeaBinding
import com.yterletskyi.happy_friend.features.ideas.model.AddIdeaModelItem

class AddIdeaAdapterDelegate : AdapterDelegate<ItemAddIdeaBinding>(
    ItemAddIdeaBinding::inflate
) {

    override fun onBindViewHolder(viewHolder: Holder<ItemAddIdeaBinding>, item: ModelItem) {
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is AddIdeaModelItem

    override fun getViewType(): Int = 2
}