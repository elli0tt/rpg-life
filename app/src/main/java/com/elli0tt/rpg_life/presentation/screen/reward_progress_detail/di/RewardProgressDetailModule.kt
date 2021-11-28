package com.elli0tt.rpg_life.presentation.screen.reward_progress_detail.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.reward_progress_detail.RewardProgressDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [RewardProgressDetailComponent::class])
abstract class RewardProgressDetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(RewardProgressDetailViewModel::class)
    abstract fun bindRewardProgressDetailViewModel(viewModel: RewardProgressDetailViewModel): ViewModel
}
