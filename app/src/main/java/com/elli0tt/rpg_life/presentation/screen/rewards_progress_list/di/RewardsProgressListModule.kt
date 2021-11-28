package com.elli0tt.rpg_life.presentation.screen.rewards_progress_list.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.rewards_progress_list.RewardsProgressListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [RewardsProgressListComponent::class])
abstract class RewardsProgressListModule {

    @Binds
    @IntoMap
    @ViewModelKey(RewardsProgressListViewModel::class)
    abstract fun bindRewardsListViewModel(viewModel: RewardsProgressListViewModel): ViewModel
}