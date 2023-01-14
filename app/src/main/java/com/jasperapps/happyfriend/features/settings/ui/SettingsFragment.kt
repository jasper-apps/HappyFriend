package com.jasperapps.happyfriend.features.settings.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jasperapps.happyfriend.common.binding.BaseBindingFragment
import com.jasperapps.happyfriend.common.list.RecyclerDelegationAdapter
import com.jasperapps.happyfriend.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseBindingFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var rvItemsAdapter: RecyclerDelegationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvItems) {
            adapter = RecyclerDelegationAdapter(view.context).apply {
                addDelegates(
                    SwitchSettingAdapter(viewModel::changeBooleanSetting),
                    TextSettingAdapter(),
                    ButtonSettingAdapter(::showChangePin),
                )
                addItemDecoration(
                    SettingsLineItemDecoration(view.context)
                )
                rvItemsAdapter = this
            }
        }
        viewModel.settingsItems.observe(viewLifecycleOwner) {
            rvItemsAdapter.setItems(it)
        }
    }

    private fun showChangePin(index: Int) {
    }
}
