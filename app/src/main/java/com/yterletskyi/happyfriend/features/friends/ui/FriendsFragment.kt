package com.yterletskyi.happyfriend.features.friends.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import com.yterletskyi.happyfriend.common.x.requestContactsPermission
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestContactsPermission({ viewModel.onContactsPermissionGranted() })

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
                onFriendSwiped = { index ->
                    val item = rvItemsAdapter.getItemTyped<FriendModelItem>(index)
                    viewModel.scheduleRemoveFriendAt(item)
                    Snackbar.make(
                        requireView(),
                        R.string.action_friend_removed,
                        Snackbar.LENGTH_SHORT
                    ).setAction(R.string.action_undo_remove_friend) {
                        viewModel.cancelRemoveFriendRequest(item)
                    }.show()
                },
                onDragEnded = {
                    val newList = rvItemsAdapter.getData()
                        .filterIsInstance<FriendModelItem>()
                    viewModel.onFriendsMoved(newList)
                },
            ).also {
                it.attachToRecyclerView(this)
                rvItemsTouchHelper = it
            }
        }

        with(binding.toolbar) {
            onActionClicked = { showSearchFragment() }
        }

        with(binding.incEmptyState.btnAddFriend) {
            setOnClickListener { showSearchFragment() }
        }

        viewModel.friendsLiveData.observe(viewLifecycleOwner) {
            val differ = FriendsDiffUtil(
                oldList = rvItemsAdapter.getDataTyped(),
                newList = it
            )
            rvItemsAdapter.setItemsWithDiff(it, differ)
        }

        viewModel.showEmptyState.observe(viewLifecycleOwner) {
            binding.incEmptyState.root.isVisible = it
            binding.rvItems.isVisible = !it
        }
    }

    private fun showIdeasScreen(index: Int) {
        val friend = rvItemsAdapter.getItemTyped<FriendModelItem>(index)
        findNavController().navigate(
            FriendsFragmentDirections.toIdeasScreen(friend.id)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friends_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                showSearchFragment(); true
            }
            else -> false
        }
    }

    private fun showSearchFragment() {
        findNavController().navigate(
            FriendsFragmentDirections.toContactsScreen()
        )
    }
}
