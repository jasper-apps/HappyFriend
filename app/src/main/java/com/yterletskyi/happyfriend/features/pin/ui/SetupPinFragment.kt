package com.yterletskyi.happyfriend.features.pin.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.binding.BaseBindingFragment
import com.yterletskyi.happyfriend.databinding.FragmentPinsetupBinding

class SetupPinFragment : BaseBindingFragment<FragmentPinsetupBinding>(
    FragmentPinsetupBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.nextButton.setOnClickListener {
            val action = SetupPinFragmentDirections.toPinScreen(getString(R.string.title), "")
            findNavController().navigate(action)
        }
    }
}


