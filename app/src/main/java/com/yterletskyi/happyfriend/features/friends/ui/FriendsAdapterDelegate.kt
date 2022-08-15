package com.yterletskyi.happyfriend.features.friends.ui

import androidx.recyclerview.widget.RecyclerView
import com.yterletskyi.happyfriend.common.list.AdapterDelegate
import com.yterletskyi.happyfriend.common.list.ModelItem
import com.yterletskyi.happyfriend.databinding.ItemFriendBinding
import com.yterletskyi.happyfriend.features.friends.domain.FriendModelItem

class FriendsAdapterDelegate(
    private val onItemClicked: (Int) -> Unit,
    private val onItemLongClicked: (RecyclerView.ViewHolder) -> Unit
) : AdapterDelegate<ItemFriendBinding>(
    ItemFriendBinding::inflate
) {

    override fun onViewHolderCreated(viewHolder: Holder<ItemFriendBinding>) {
        with(viewHolder) {
            binding.root.setOnClickListener {
                onItemClicked(adapterPosition)
            }
            binding.root.setOnLongClickListener {
                onItemLongClicked(this)
                true
            }
        }
    }

    override fun onBindViewHolder(viewHolder: Holder<ItemFriendBinding>, item: ModelItem) {
        item as FriendModelItem

        with(viewHolder.binding) {
            image.setImageDrawable(item.image)
            text.text = item.fullName
            birthday.text = item.birthday
        }
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is FriendModelItem

    override fun getViewType(): Int = FRIEND_ITEM_VIEW_TYPE

    companion object {
        const val FRIEND_ITEM_VIEW_TYPE = 1
    }
}
