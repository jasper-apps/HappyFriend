package com.yterletskyi.happy_friend.features.contacts.ui

import com.yterletskyi.happy_friend.common.list.AdapterDelegate
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.databinding.ItemContactBinding
import com.yterletskyi.happy_friend.features.contacts.domain.ContactModelItem

class ContactsAdapterDelegate(
    private val onStarClicked: (Int) -> Unit
) : AdapterDelegate<ItemContactBinding>(
    ItemContactBinding::inflate
) {

    override fun onViewHolderCreated(viewHolder: Holder<ItemContactBinding>) {
        viewHolder.binding.starBtn.setOnClickListener {
            onStarClicked(viewHolder.adapterPosition)
        }
    }

    override fun onBindViewHolder(viewHolder: Holder<ItemContactBinding>, item: ModelItem) {
        item as ContactModelItem

        viewHolder.binding.image.setImageDrawable(item.image)
        viewHolder.binding.text.text = item.fullName
        viewHolder.binding.birthday.text = item.birthday.toString()
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is ContactModelItem

    override fun getViewType(): Int = 1
}