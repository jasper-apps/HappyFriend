package com.yterletskyi.happyfriend.features.pin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.binding.BaseBindingFragment
import com.yterletskyi.happyfriend.databinding.FragmentPinBinding
import com.yterletskyi.happyfriend.features.pin.data.PinCode
import com.yterletskyi.happyfriend.features.pin.data.PinCodeController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PinFragment : BaseBindingFragment<FragmentPinBinding>(
    FragmentPinBinding::inflate
) {
    @Inject
    lateinit var pinCodeController: PinCodeController

    private val viewModel: PinViewModel by viewModels()
    private val args: PinFragmentArgs by navArgs()
    private var pin: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pinKeyboardView.onButtonClicked = viewModel::input

        with(binding.pinTitle) {
            text = if(args.title != null) {
                args.title
            } else {
                getString(R.string.pin_enter_title)
            }
        }

        viewModel.pinMaxLengthLiveData.observe(viewLifecycleOwner) { length ->
            binding.pinProgressView.steps = length

        }

        viewModel.pinProgressLiveData.observe(viewLifecycleOwner) { progress ->
            binding.pinProgressView.step = progress
            authorize(progress)
        }
    }

    private fun authorize(progress: Int) {
        when (progress) {
            MAX_LENGTH -> {
                if (pinCodeController.getPinCode() == null) {
                    val action = PinFragmentDirections.toPinScreen(
                        getString(R.string.pin_repeat_title), "",
                        viewModel.pinProgressLiveData.value.toString()
                    )
                    pinCodeController.savePinCode(PinCode(pin))
                    findNavController().navigate(action)
                } else {
                    when (pinCodeController.getPinCode()!!.pin) {
                        pin -> {
                            findNavController().navigate(PinFragmentDirections.toContactsScreen())
                        }
                        else -> {
                            findNavController().navigate(
                                PinFragmentDirections.toPinScreen(
                                    getString(
                                        R.string.pin_enter_title
                                    ),
                                    getString(R.string.pin_enter_error),
                                    ""
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val MAX_LENGTH = 4
    }
}
