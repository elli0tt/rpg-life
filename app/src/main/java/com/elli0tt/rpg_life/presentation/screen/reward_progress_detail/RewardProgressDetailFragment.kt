package com.elli0tt.rpg_life.presentation.screen.reward_progress_detail

import android.os.Bundle
import android.view.View
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import com.elli0tt.rpg_life.presentation.screen.reward_progress_detail.di.RewardProgressDetailComponent

class RewardProgressDetailFragment : BaseFragment(R.layout.fragment_reward_progress_detail) {

    private lateinit var rewardProgressDetailComponent: RewardProgressDetailComponent

    private lateinit var viewModel: RewardProgressDetailViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()
        initViews()
        subscribeToViewModel()
    }

    private fun initDagger() {
        rewardProgressDetailComponent = appComponent.rewardProgressDetailComponentFactory().create()
        rewardProgressDetailComponent.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }

    private fun initViews() {

    }

    private fun subscribeToViewModel() {

    }
}