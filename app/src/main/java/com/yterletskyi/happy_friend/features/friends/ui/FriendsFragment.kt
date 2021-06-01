package com.yterletskyi.happy_friend.features.friends.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yterletskyi.happy_friend.R
import com.yterletskyi.happy_friend.common.binding.BaseBindingFragment
import com.yterletskyi.happy_friend.common.list.RecyclerDelegationAdapter
import com.yterletskyi.happy_friend.common.list.SpaceItemDecoration
import com.yterletskyi.happy_friend.common.x.dp
import com.yterletskyi.happy_friend.databinding.FragmentFriendsBinding
import com.yterletskyi.happy_friend.features.friends.domain.FriendModelItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : BaseBindingFragment<FragmentFriendsBinding>(
    FragmentFriendsBinding::inflate
) {

    private val viewModel by viewModels<FriendsViewModel>()
    private lateinit var rvItemsAdapter: RecyclerDelegationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvItems) {
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerDelegationAdapter(context).apply {
                addDelegate(
                    FriendsAdapterDelegate(
                        onItemClicked = ::showIdeasScreen
                    )
                )
                rvItemsAdapter = this
                addItemDecoration(
                    SpaceItemDecoration(space = 8.dp)
                )
            }
        }

        with(binding.toolbar) {
            onActionClicked = { showSearchFragment() }
        }

        viewModel.friends.observe(viewLifecycleOwner, Observer {
            rvItemsAdapter.setItems(it)
        })

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
