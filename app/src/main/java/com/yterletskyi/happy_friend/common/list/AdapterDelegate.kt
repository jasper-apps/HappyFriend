package com.yterletskyi.happy_friend.common.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.yterletskyi.happy_friend.common.binding.Inflate

abstract class AdapterDelegate<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) {

    class Holder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    abstract fun onBindViewHolder(viewHolder: Holder<VB>, item: ModelItem)

    abstract fun isForViewType(item: ModelItem, position: Int): Boolean

    abstract fun getViewType(): Int

    @CallSuper
    open fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        _binding = inflate(inflater, parent, false)
        val holder = Holder(binding)
        onViewHolderCreated(holder)
        return holder
    }

    open fun onBindViewHolder(
        viewHolder: Holder<VB>,
        item: ModelItem,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(viewHolder, item)
    }

    open fun onViewHolderCreated(viewHolder: Holder<VB>) {}

    open fun onRecycled(viewHolder: RecyclerView.ViewHolder) {}

    @CallSuper
    open fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        _binding = null
    }

}