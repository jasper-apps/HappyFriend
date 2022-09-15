package com.jasperapps.happyfriend.features.ideas.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jasperapps.happyfriend.common.binding.BaseBindingFragment
import com.jasperapps.happyfriend.common.list.RecyclerDelegationAdapter
import com.jasperapps.happyfriend.databinding.FragmentIdeasBinding
import com.jasperapps.happyfriend.features.ideas.domain.IdeaModelItem
import com.jasperapps.happyfriend.features.ideas.domain.IdeasDiffUtil
import com.jasperapps.happyfriend.features.ideas.ui.drag.MoveIdeaTouchHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdeasFragment : BaseBindingFragment<FragmentIdeasBinding>(
    FragmentIdeasBinding::inflate
) {

    private val viewModel by viewModels<IdeasViewModel>()

    private lateinit var rvItemsTouchHelper: MoveIdeaTouchHelper
    private lateinit var rvItemsAdapter: RecyclerDelegationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvItems) {
            adapter = RecyclerDelegationAdapter(context).apply {
                addDelegate(
                    IdeasAdapterDelegate(
                        onTextChanged = { i, t -> viewModel.updateIdea(i, t.trim()) },
                        onCheckboxChanged = { i, c -> viewModel.updateIdea(i, c) },
                        onRemoveClicked = { i -> viewModel.removeIdea(i) },
                        onNewIdeaClicked = viewModel::addIdea,
                        onRemoveIdeaClicked = viewModel::removeIdea,
                        onGripLongClicked = { rvItemsTouchHelper.startDrag(it) },
                    )
                )
                addDelegate(
                    AddIdeaAdapterDelegate(
                        onItemClicked = viewModel::addIdea
                    )
                )
                rvItemsAdapter = this
            }
            layoutManager = LinearLayoutManager(context)
            MoveIdeaTouchHelper(
                onIdeaMoved = rvItemsAdapter::swapItems,
                onDragEnded = {
                    val ideas = rvItemsAdapter.getData()
                        .filterIsInstance<IdeaModelItem>()
                    viewModel.onIdeasMoved(ideas)
                }
            ).also {
                it.attachToRecyclerView(this)
                rvItemsTouchHelper = it
            }
        }

        with(binding.toolbar) {
            onBackClicked = { findNavController().popBackStack() }
        }

        viewModel.title.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
        }

        viewModel.ideasLiveData.observe(viewLifecycleOwner) {
            val diffUtil = IdeasDiffUtil(
                oldList = rvItemsAdapter.getData(),
                newList = it
            )
            rvItemsAdapter.setItemsWithDiff(it, diffUtil)
        }
    }
}
