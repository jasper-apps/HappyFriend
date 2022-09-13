package com.jasperapps.happyfriend.features.contacts.ui

import com.jasperapps.happyfriend.R
import com.jasperapps.happyfriend.common.list.AdapterDelegate
import com.jasperapps.happyfriend.common.list.ModelItem
import com.jasperapps.happyfriend.databinding.ItemContactBinding
import com.jasperapps.happyfriend.features.contacts.domain.ContactModelItem

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
            birthday.text = item.birthday
            starBtn.setImageResource(
                if (item.isFriend) R.drawable.ic_baseline_favorite_24_selected
                else R.drawable.ic_baseline_favorite_24
            )
        }
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is ContactModelItem

    override fun getViewType(): Int = 1
}
