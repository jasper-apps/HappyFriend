package com.yterletskyi.happy_friend.features.ideas.ui

import com.yterletskyi.happy_friend.common.list.AdapterDelegate
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.databinding.ItemAddIdeaBinding
import com.yterletskyi.happy_friend.features.ideas.domain.AddIdeaModelItem

class AddIdeaAdapterDelegate(
    private val onItemClicked: () -> Unit
) : AdapterDelegate<ItemAddIdeaBinding>(
    ItemAddIdeaBinding::inflate
) {

    override fun onViewHolderCreated(viewHolder: Holder<ItemAddIdeaBinding>) {
        viewHolder.itemView.setOnClickListener { onItemClicked() }
    }

    override fun onBindViewHolder(viewHolder: Holder<ItemAddIdeaBinding>, item: ModelItem) {
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is AddIdeaModelItem

    override fun getViewType(): Int = 2
}