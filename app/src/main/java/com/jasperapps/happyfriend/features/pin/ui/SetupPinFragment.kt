package com.jasperapps.happyfriend.features.pin.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.jasperapps.happyfriend.R
import com.jasperapps.happyfriend.common.binding.BaseBindingFragment
import com.jasperapps.happyfriend.databinding.FragmentPinsetupBinding

class SetupPinFragment : BaseBindingFragment<FragmentPinsetupBinding>(
    FragmentPinsetupBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.textButton) {
            setOnClickListener {
                findNavController().navigate(
                    SetupPinFragmentDirections.toPinScreen(
                        title = getString(R.string.pin_create_title)
                    )
                )
            }
        }
    }
}
