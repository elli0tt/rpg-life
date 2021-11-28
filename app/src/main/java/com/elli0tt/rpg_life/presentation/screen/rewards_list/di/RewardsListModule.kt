package com.elli0tt.rpg_life.presentation.screen.rewards_list.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.rewards_list.RewardsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [RewardsListComponent::class])
abstract class RewardsListModule {

    @Binds
    @IntoMap
    @ViewModelKey(RewardsListViewModel::class)
    abstract fun bindRewardsListViewModel(viewModel: RewardsListViewModel): ViewModel
}