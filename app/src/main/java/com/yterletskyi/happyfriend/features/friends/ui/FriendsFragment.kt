package com.yterletskyi.happyfriend.features.friends.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.binding.BaseBindingFragment
import com.yterletskyi.happyfriend.common.list.RecyclerDelegationAdapter
import com.yterletskyi.happyfriend.common.list.SpaceItemDecoration
import com.yterletskyi.happyfriend.common.x.dp
import com.yterletskyi.happyfriend.databinding.FragmentFriendsBinding
import com.yterletskyi.happyfriend.features.friends.domain.FriendModelItem
import com.yterletskyi.happyfriend.features.friends.domain.FriendsDiffUtil
import com.yterletskyi.happyfriend.features.friends.ui.drag.FriendsTouchHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : BaseBindingFragment<FragmentFriendsBinding>(
    FragmentFriendsBinding::inflate
) {

    private val viewModel by viewModels<FriendsViewModel>()

    private lateinit var rvItemsTouchHelper: FriendsTouchHelper
    private lateinit var rvItemsAdapter: RecyclerDelegationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermissions()

        with(binding.rvItems) {
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerDelegationAdapter(context).apply {
                addDelegate(
                    FriendsAdapterDelegate(
                        onItemClicked = ::showIdeasScreen,
                        onItemLongClicked = { rvItemsTouchHelper.startDrag(it) }
                    )
                )
                rvItemsAdapter = this
                addItemDecoration(
                    SpaceItemDecoration(space = 4.dp)
                )
            }
            FriendsTouchHelper(
                onFriendMoved = rvItemsAdapter::swapItems,
                onDragEnded = {
                    val newList = rvItemsAdapter.getData()
                        .filterIsInstance<FriendModelItem>()
                    viewModel.onFriendsMoved(newList)
                },
                onFriendSwiped = { index ->
                    viewModel.scheduleRemoveFriendAt(index)
                    Snackbar.make(
                        requireView(),
                        R.string.action_friend_removed,
                        Snackbar.LENGTH_SHORT
                    ).setAction(R.string.action_undo_remove_friend) {
                        viewModel.cancelRemoveFriendRequest(index)
                        rvItemsAdapter.notifyItemChanged(index)
                    }.show()
                }
            ).also {
                it.attachToRecyclerView(this)
                rvItemsTouchHelper = it
            }
        }

        viewModel.showEmptyState.observe(viewLifecycleOwner) {
            binding.incEmptyState.root.isVisible = it
            binding.rvItems.isVisible = !it
        }
    }

    private fun requestPermissions() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                viewModel.friends.observe(viewLifecycleOwner) {
                    val differ = FriendsDiffUtil(
                        oldList = rvItemsAdapter.getDataTyped(),
                        newList = it
                    )
                    rvItemsAdapter.setItemsWithDiff(it, differ)
                }
            } else {
                Toast.makeText(context, "Please grant permission", Toast.LENGTH_SHORT).show()
            }
        }.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun showIdeasScreen(index: Int) {
        val friend = rvItemsAdapter.getItemTyped<FriendModelItem>(index)
        findNavController().navigate(
            FriendsFragmentDirections.toIdeasScreen(friend.id)
        )
    }
}
