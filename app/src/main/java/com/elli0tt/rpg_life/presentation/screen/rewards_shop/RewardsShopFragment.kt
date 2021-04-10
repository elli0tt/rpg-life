package com.elli0tt.rpg_life.presentation.screen.rewards_shop

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.adapter.rewards_shop.RewardsShopAdapter
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_rewards_shop.*

class RewardsShopFragment : BaseFragment(R.layout.fragment_rewards_shop) {

    private lateinit var viewModel: RewardsShopViewModel

    private val rewardsAdapter = RewardsShopAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RewardsShopViewModel::class.java)

        initViews()
        subscribeToViewModel()
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
        }
    }
}