package com.elli0tt.rpg_life.presentation.screen.rewards_shop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.adapter.rewards_shop.RewardsShopAdapter
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import com.elli0tt.rpg_life.presentation.screen.rewards_shop.di.RewardsShopComponent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rewards_shop.*
import javax.inject.Inject

class RewardsShopFragment : BaseFragment(R.layout.fragment_rewards_shop) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var rewardsShopComponent: RewardsShopComponent

    private lateinit var viewModel: RewardsShopViewModel

    private val rewardsAdapter = RewardsShopAdapter().apply {
        onItemClickListener = RewardsShopAdapter.OnItemClickListener {
            showBuyDialog(position = it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()
        initViews()
        subscribeToViewModel()
    }

    private fun initDagger() {
        rewardsShopComponent = appComponent.rewardsShopComponentFactory().create()
        rewardsShopComponent.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }

    private fun initViews() {
        rewardsRecyclerView.apply {
            adapter = rewardsAdapter
            setHasFixedSize(true)
        }
    }

    private fun subscribeToViewModel() {
        viewModel.apply {
            rewardsList.observe(viewLifecycleOwner) {
                rewardsAdapter.submitList(it)
            }
            showSnackbarEvent.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { text ->
                    Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showBuyDialog(position: Int) {
        AlertDialog.Builder(requireContext())
                .setTitle(viewModel.rewardsList.value?.get(position)?.name ?: "")
                .setPositiveButton(R.string.rewards_shop_buy_button_text) { _, _ ->
                    viewModel.buyReward(position)
                }
                .setNeutralButton(R.string.rewards_shop_cancel_button_text) { _, _ ->
                    // do nothing
                }
                .show()
    }
}