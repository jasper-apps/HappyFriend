package com.yterletskyi.happy_friend.features.friends.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.yterletskyi.happy_friend.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<FriendsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.friends.observe(viewLifecycleOwner, Observer {
            val text: String = it.map { it.firstName }.reduce { acc, friend -> "$acc $friend" }
            view.findViewById<TextView>(R.id.text_home).text = text

        })
    }

}