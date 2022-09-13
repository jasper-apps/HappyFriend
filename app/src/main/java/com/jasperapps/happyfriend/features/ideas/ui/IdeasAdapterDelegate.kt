package com.jasperapps.happyfriend.features.ideas.ui

import android.graphics.Paint
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.jasperapps.happyfriend.common.list.AdapterDelegate
import com.jasperapps.happyfriend.common.list.ModelItem
import com.jasperapps.happyfriend.common.x.focus
import com.jasperapps.happyfriend.databinding.ItemIdeaBinding
import com.jasperapps.happyfriend.features.ideas.domain.IdeaModelItem

class IdeasAdapterDelegate(
    private val onTextChanged: (Int, String) -> Unit,
    private val onCheckboxChanged: (Int, Boolean) -> Unit,
    private val onRemoveClicked: (Int) -> Unit,
    private val onNewIdeaClicked: (String) -> Unit,
    private val onRemoveIdeaClicked: (Int) -> Unit,
    private val onGripLongClicked: (RecyclerView.ViewHolder) -> Unit,
) : AdapterDelegate<ItemIdeaBinding>(
    ItemIdeaBinding::inflate
) {

    override fun onViewHolderCreated(viewHolder: Holder<ItemIdeaBinding>) {
        with(viewHolder) {
            with(binding.input) {
                doAfterTextChanged {
                    onTextChanged(adapterPosition, binding.input.text.toString())
                }
                onNewIdeaRequested = onNewIdeaClicked
                onRemoveIdeaRequested = { onRemoveIdeaClicked(adapterPosition) }
                onFocusChanged = { binding.remove.isVisible = it }
            }
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxChanged(adapterPosition, isChecked)
                updateStrikethrough(binding.input, isChecked)
            }
            binding.remove.setOnClickListener {
                onRemoveClicked(adapterPosition)
            }
            binding.grip.setOnLongClickListener {
                onGripLongClicked(this)
                true
            }
        }
    }

    override fun onBindViewHolder(viewHolder: Holder<ItemIdeaBinding>, item: ModelItem) {
        item as IdeaModelItem

        with(viewHolder.binding) {
            checkbox.isChecked = item.done
            remove.isVisible = item.focused
            with(input) {
                setText(item.text)
                updateStrikethrough(this, item.done)
                if (item.focused) focus()
            }
        }
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is IdeaModelItem

    override fun getViewType(): Int = IDEA_ITEM_VIEW_TYPE

    private fun updateStrikethrough(view: TextView, isChecked: Boolean) {
        with(view) {
            paintFlags =
                if (isChecked) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    companion object {
        const val IDEA_ITEM_VIEW_TYPE = 1
    }
}
