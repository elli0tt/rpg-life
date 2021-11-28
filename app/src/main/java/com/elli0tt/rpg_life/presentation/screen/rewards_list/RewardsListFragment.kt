package com.elli0tt.rpg_life.presentation.screen.rewards_list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.adapter.rewards_list.RewardsListAdapter
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import com.elli0tt.rpg_life.presentation.screen.rewards_list.di.RewardsListComponent
import kotlinx.android.synthetic.main.fragment_rewards_list.*

class RewardsListFragment : BaseFragment(R.layout.fragment_rewards_list) {

    private lateinit var rewardsListComponent: RewardsListComponent

    private lateinit var viewModel: RewardsListViewModel

    private val rewardsRecyclerAdapter = RewardsListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()
        initViews()
        subscribeToViewModel()
    }

    private fun initDagger() {
        rewardsListComponent = appComponent.rewardsListComponent().create()
        rewardsListComponent.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }

    private fun initViews() {
        initRewardsRecyclerView()
    }

    private fun initRewardsRecyclerView() {
        rewardsRecyclerView.apply {
            setHasFixedSize(true)
            adapter = rewardsRecyclerAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
    }

    private fun subscribeToViewModel() {
        viewModel.rewardsList.observe(viewLifecycleOwner) {
            rewardsRecyclerAdapter.submitList(it)
        }
    }
}