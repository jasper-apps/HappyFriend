package com.yterletskyi.happy_friend.features.friends.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yterletskyi.happy_friend.common.binding.BaseBindingFragment
import com.yterletskyi.happy_friend.common.list.RecyclerDelegationAdapter
import com.yterletskyi.happy_friend.common.list.SpaceItemDecoration
import com.yterletskyi.happy_friend.common.x.dp
import com.yterletskyi.happy_friend.databinding.FragmentFriendsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : BaseBindingFragment<FragmentFriendsBinding>(
    FragmentFriendsBinding::inflate
) {

    private val viewModel by viewModels<FriendsViewModel>()
    private lateinit var rvItemsAdapter: RecyclerDelegationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvItems) {
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerDelegationAdapter(context).apply {
                addDelegate(
                    FriendsAdapterDelegate()
                )
                rvItemsAdapter = this
                addItemDecoration(
                    SpaceItemDecoration(space = 8.dp)
                )
            }
        }

        val callback = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                viewModel.friends.observe(viewLifecycleOwner, Observer {
                    rvItemsAdapter.setItems(it)
                })
            } else {
                Toast.makeText(context, "Please grant permission", Toast.LENGTH_SHORT).show()
            }
        }
        callback.launch(Manifest.permission.READ_CONTACTS)

    }

}