package com.jasperapps.happyfriend.features.pin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jasperapps.happyfriend.common.binding.BaseBindingFragment
import com.jasperapps.happyfriend.databinding.FragmentPinBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
classPinFragment : BaseBindingFragment<FragmentPinBinding>(
    FragmentPinBinding::inflate
) {

    private val viewModel: PinViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pinKeyboardView.onButtonClicked = viewModel::input

        viewModel.titleLiveData.observe(viewLifecycleOwner) { titleResId ->
            binding.pinTitle.text = getString(titleResId)
        }

        viewModel.pinMaxLengthLiveData.observe(viewLifecycleOwner) { length ->
            binding.pinProgressView.steps = length
        }

        viewModel.pinProgressLiveData.observe(viewLifecycleOwner) { progress ->
            binding.pinProgressView.step = progress
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            binding.pinProgressView.step = 0
            binding.pinError.text = getString(error)
        }
        viewModel.directionsLiveData.observe(viewLifecycleOwner) { direction ->
            findNavController().navigate(direction)
        }
    }
}
