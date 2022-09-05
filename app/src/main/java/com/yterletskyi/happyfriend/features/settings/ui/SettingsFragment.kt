package com.yterletskyi.happyfriend.features.settings.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.yterletskyi.happyfriend.common.binding.BaseBindingFragment
import com.yterletskyi.happyfriend.common.list.RecyclerDelegationAdapter
import com.yterletskyi.happyfriend.databinding.FragmentSettingsBinding
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
                addDelegate(
                    SwitchSettingAdapter(viewModel::changeBooleanSetting)
                )
                addItemDecoration(
                    SettingsLineItemDecoration(view.context)
                )
                addDelegate(
                    TextSettingAdapter()
                )
                rvItemsAdapter = this
            }
        }
        viewModel.settingsItems.observe(viewLifecycleOwner) {
            rvItemsAdapter.setItems(it)
        }
    }
}
