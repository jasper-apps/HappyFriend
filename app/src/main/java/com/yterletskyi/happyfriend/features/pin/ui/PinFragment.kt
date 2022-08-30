package com.yterletskyi.happyfriend.features.pin.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.yterletskyi.happyfriend.common.binding.BaseBindingFragment
import com.yterletskyi.happyfriend.databinding.FragmentPinBinding

class PinFragment : BaseBindingFragment<FragmentPinBinding>(
    FragmentPinBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pinKeyboardView.onButtonClicked = {
            Log.i("info20", it.toString())
        }
    }
}
