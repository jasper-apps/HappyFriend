package com.yterletskyi.happyfriend.features.ideas.ui

import com.yterletskyi.happyfriend.common.list.AdapterDelegate
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.databinding.ItemAddIdeaBinding
import com.yterletskyi.happyfriend.features.ideas.domain.AddIdeaModelItem

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

    override fun getViewType(): Int = ADD_IDEA_ITEM_VIEW_TYPE

    companion object {
        const val ADD_IDEA_ITEM_VIEW_TYPE = 2
    }
}
