package com.yterletskyi.happyfriend.features.pin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yterletskyi.happyfriend.common.binding.BaseBindingFragment
import com.yterletskyi.happyfriend.databinding.FragmentPinBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PinFragment : BaseBindingFragment<FragmentPinBinding>(
    FragmentPinBinding::inflate
) {

    private val viewModel: PinViewModel by viewModels()
    private val args: PinFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pinKeyboardView.onButtonClicked = viewModel::input

        with(binding.pinTitle) {
            text = getString(args.title)
        }

        viewModel.pinMaxLengthLiveData.observe(viewLifecycleOwner) { length ->
            binding.pinProgressView.steps = length
        }

        viewModel.pinProgressLiveData.observe(viewLifecycleOwner) { progress ->
            binding.pinProgressView.step = progress
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            binding.pinError.text = getString(error)
        }
        viewModel.directionsData.observe(viewLifecycleOwner) { direction ->
            findNavController().navigate(direction)
        }
    }
}
