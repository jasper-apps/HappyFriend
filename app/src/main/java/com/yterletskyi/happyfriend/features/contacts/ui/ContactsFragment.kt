package com.yterletskyi.happyfriend.features.contacts.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yterletskyi.happyfriend.common.binding.BaseBindingFragment
import com.yterletskyi.happyfriend.common.list.RecyclerDelegationAdapter
import com.yterletskyi.happyfriend.common.list.SpaceItemDecoration
import com.yterletskyi.happyfriend.common.x.dp
import com.yterletskyi.happyfriend.databinding.FragmentContactsBinding
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
                    SpaceItemDecoration(space = 4.dp)
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

        with(binding.etSearch) {
            binding.etClear.visibility = View.INVISIBLE
            doAfterTextChanged {
                if (text?.isEmpty() == false) {
                    binding.etClear.visibility = View.VISIBLE
                } else {
                    binding.etClear.visibility = View.INVISIBLE
                }
            }
        }

        with(binding.etClear) {
            setOnClickListener {
                binding.etSearch.setText("")
            }
        }

        viewModel.contacts.observe(viewLifecycleOwner, rvItemsAdapter::setItems)
    }
}
