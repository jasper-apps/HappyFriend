package com.yterletskyi.happy_friend.features.ideas.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yterletskyi.happy_friend.common.binding.BaseBindingFragment
import com.yterletskyi.happy_friend.common.list.RecyclerDelegationAdapter
import com.yterletskyi.happy_friend.databinding.FragmentIdeasBinding
import com.yterletskyi.happy_friend.features.ideas.model.IdeasInteractor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IdeasFragment : BaseBindingFragment<FragmentIdeasBinding>(
    FragmentIdeasBinding::inflate
) {

    private val args by navArgs<IdeasFragmentArgs>()

    @Inject
    lateinit var interactor: IdeasInteractor

    @Inject
    lateinit var myViewModelAssistedFactory: IdeasViewModel.IdeasViewModelAssistedFactory
    private val viewModel by viewModels<IdeasViewModel> {
        IdeasViewModel.provideFactory(
            myViewModelAssistedFactory,
            args.friendId,
            interactor
        )
    }

    private lateinit var rvItemsAdapter: RecyclerDelegationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvItems) {
            adapter = RecyclerDelegationAdapter(context).apply {
                addDelegate(IdeasAdapterDelegate())
                rvItemsAdapter = this
            }
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.ideas.observe(viewLifecycleOwner, Observer {
            rvItemsAdapter.setItems(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}