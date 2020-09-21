package com.elli0tt.rpg_life.presentation.screen.rewards_shop

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elli0tt.rpg_life.R
import kotlinx.android.synthetic.main.fragment_rewards_shop.*

class RewardsShopFragment : Fragment(R.layout.fragment_rewards_shop) {

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
            rewardsList.observe(viewLifecycleOwner, Observer {
                rewardsAdapter.submitList(it)
            })
        }
    }
}