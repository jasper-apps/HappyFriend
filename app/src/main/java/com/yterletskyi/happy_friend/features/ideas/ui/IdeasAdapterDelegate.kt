package com.yterletskyi.happy_friend.features.ideas.ui

import androidx.core.widget.doAfterTextChanged
import com.yterletskyi.happy_friend.common.list.AdapterDelegate
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.databinding.ItemIdeaBinding
import com.yterletskyi.happy_friend.features.ideas.model.IdeaModelItem

class IdeasAdapterDelegate(
    private val onTextChanged: (Int, String) -> Unit,
    private val onCheckboxChanged: (Int, Boolean) -> Unit,
    private val onRemoveClicked: (Int) -> Unit
) : AdapterDelegate<ItemIdeaBinding>(
    ItemIdeaBinding::inflate
) {

    override fun onViewHolderCreated(viewHolder: Holder<ItemIdeaBinding>) {
        with(viewHolder) {
            binding.input.doAfterTextChanged {
                onTextChanged(adapterPosition, binding.input.text.toString())
            }
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxChanged(adapterPosition, isChecked)
            }
            binding.remove.setOnClickListener {
                onRemoveClicked(adapterPosition)
            }
        }
    }

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