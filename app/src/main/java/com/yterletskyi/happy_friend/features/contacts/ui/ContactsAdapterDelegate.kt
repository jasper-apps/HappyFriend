package com.yterletskyi.happy_friend.features.contacts.ui

import com.yterletskyi.happy_friend.R
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

        with(viewHolder.binding) {
            image.setImageDrawable(item.image)
            text.text = item.fullName
            birthday.text = item.birthday.toString()
            starBtn.setImageResource(
                if (item.isFriend) R.drawable.ic_baseline_favorite_24
                else R.drawable.ic_baseline_favorite_border_24
            )
        }
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is ContactModelItem

    override fun getViewType(): Int = 1
}
