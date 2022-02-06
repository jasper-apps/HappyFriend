package com.yterletskyi.happy_friend.features.contacts.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yterletskyi.happy_friend.common.binding.BaseBindingFragment
import com.yterletskyi.happy_friend.common.list.RecyclerDelegationAdapter
import com.yterletskyi.happy_friend.common.list.SpaceItemDecoration
import com.yterletskyi.happy_friend.common.x.dp
import com.yterletskyi.happy_friend.databinding.FragmentContactsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : BaseBindingFragment<FragmentContactsBinding>(
    FragmentContactsBinding::inflate
) {

    private val viewModel by viewModels<ContactsViewModel>()
    private lateinit var rvItemsAdapter: RecyclerDelegationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvItems) {
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerDelegationAdapter(context).apply {
                addDelegate(
                    ContactsAdapterDelegate(
                        onStarClicked = viewModel::toggleFriend
                    )
                )
                rvItemsAdapter = this
                addItemDecoration(
                    SpaceItemDecoration(space = 8.dp)
                )
            }
        }

        with(binding.toolbar) {
            onBackClicked = { findNavController().popBackStack() }
        }

        with(binding.etSearch) {
            doAfterTextChanged {
                it?.let {
                    viewModel.search(it.toString())
                }
            }
        }

        viewModel.contacts.observe(viewLifecycleOwner, rvItemsAdapter::setItems)
    }

}
