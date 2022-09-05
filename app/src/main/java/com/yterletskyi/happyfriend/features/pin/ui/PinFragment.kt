package com.yterletskyi.happyfriend.features.pin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.binding.BaseBindingFragment
import com.yterletskyi.happyfriend.databinding.FragmentPinBinding

class PinFragment : BaseBindingFragment<FragmentPinBinding>(
    FragmentPinBinding::inflate
) {

    private val viewModel: PinViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pinKeyboardView.onButtonClicked = viewModel::input

        viewModel.pinMaxLengthLiveData.observe(viewLifecycleOwner) { length ->
            binding.pinProgressView.steps = length
        }

        viewModel.pinProgressLiveData.observe(viewLifecycleOwner) { progress ->
            binding.pinProgressView.step = progress
        }

        with(binding.pinTitle) {
            text = getString(R.string.pin_enter_title)
        }
    }
}
