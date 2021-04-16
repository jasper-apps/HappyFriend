package com.yterletskyi.happy_friend.common.list

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.core.util.set
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import kotlin.reflect.KClass

@Suppress("unused")
open class RecyclerDelegationAdapter(
    protected val context: Context,
    private val scope: CoroutineScope? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val delegates: MutableList<AdapterDelegate<ViewBinding>> = ArrayList()
    private val itemTypeToDelegatesMap = SparseArray<AdapterDelegate<ViewBinding>>()
    protected val items: MutableList<ModelItem> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var diffJob: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        itemTypeToDelegatesMap[viewType].onCreateViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegate = itemTypeToDelegatesMap[holder.itemViewType]
        delegate.onBindViewHolder(holder as AdapterDelegate.Holder<ViewBinding>, items[position])
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val delegate = itemTypeToDelegatesMap[holder.itemViewType]
        delegate.onBindViewHolder(
            holder as AdapterDelegate.Holder<ViewBinding>,
            items[position],
            payloads
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        delegates.forEach { it.onDetachedFromRecyclerView(recyclerView) }
        this.recyclerView = null
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        itemTypeToDelegatesMap[holder.itemViewType].onRecycled(holder)
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        val delegate = delegates
            .find { delegate -> delegate.isForViewType(item, position) }
            ?: throw NullPointerException("Delegate for item in position $position not found")

        val viewType = delegate.getViewType()
        itemTypeToDelegatesMap[viewType] = delegate
        return viewType
    }

    fun <D : AdapterDelegate<*>> addDelegate(delegate: D) {
        delegates.add(delegate as AdapterDelegate<ViewBinding>)
    }

    fun <D : AdapterDelegate<*>> addDelegates(vararg delegatesToAdd: D) {
        delegates.addAll(delegatesToAdd as Array<out AdapterDelegate<ViewBinding>>)
    }

    fun <D : AdapterDelegate<*>> removeDelegate(delegate: D) {
        delegates.remove(delegate as AdapterDelegate<ViewBinding>)
    }

    fun setItems(items: List<ModelItem>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItemToEnd(item: ModelItem) {
        val index = items.size
        addItem(index, item)
    }

    fun addItem(index: Int, item: ModelItem) {
        items.add(index, item)
        notifyItemInserted(index)
    }

    fun removeItem(item: ModelItem) {
        val position = items.indexOf(item)
        if (position >= 0) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun smoothScrollToPosition(position: Int) {
        recyclerView?.smoothScrollToPosition(position)
    }

    fun setItemsWithDiff(
        items: List<ModelItem>,
        callback: DiffUtil.Callback,
        detectMoves: Boolean = true,
        updatesDispatched: (() -> Unit)? = null
    ) {
        if (scope == null) {
            setItemsWithDiffInternal(items, callback, detectMoves, updatesDispatched)
            return
        }
        diffJob?.cancel()
        diffJob = scope.launch {
            val diffResult = withContext(Dispatchers.Default) {
                DiffUtil.calculateDiff(callback, detectMoves)
            }
            this@RecyclerDelegationAdapter.items.clear()
            this@RecyclerDelegationAdapter.items.addAll(items)
            diffResult.dispatchUpdatesTo(this@RecyclerDelegationAdapter)
            updatesDispatched?.invoke()
        }
    }

    private fun setItemsWithDiffInternal(
        items: List<ModelItem>,
        callback: DiffUtil.Callback,
        detectMoves: Boolean = true,
        updatesDispatched: (() -> Unit)? = null
    ) {
        val diffResult = DiffUtil.calculateDiff(callback, detectMoves)
        this@RecyclerDelegationAdapter.items.clear()
        this@RecyclerDelegationAdapter.items.addAll(items)
        diffResult.dispatchUpdatesTo(this@RecyclerDelegationAdapter)
        updatesDispatched?.invoke()
    }

    fun addItemsToEnd(newItems: List<ModelItem>) {
        val lastPosition = this.items.size
        this.items.addAll(newItems)
        this.notifyItemRangeInserted(lastPosition, newItems.size - 1)
    }

    fun getData(): List<ModelItem> = this.items

    @Suppress("UNCHECKED_CAST")
    fun <T : ModelItem> getDataTyped(): List<T> = this.items as List<T>

    fun getItem(position: Int) = this.items[position]

    @Suppress("UNCHECKED_CAST")
    fun <T : ModelItem> getItemTyped(position: Int): T = this.items[position] as T

    fun <D : AdapterDelegate<ViewBinding>> getDelegatesTyped(kClass: KClass<D>): List<D> {
        return delegates.filterIsInstance(kClass.java)
    }

    fun <D : AdapterDelegate<ViewBinding>> getDelegateTyped(kClass: KClass<D>): D? {
        return delegates.filterIsInstance(kClass.java).firstOrNull()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }
}