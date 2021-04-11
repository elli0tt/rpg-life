package com.elli0tt.rpg_life.presentation.screen.rewards_shop.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.rewards_shop.RewardsShopViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RewardsShopModule {

    @Binds
    @IntoMap
    @ViewModelKey(RewardsShopViewModel::class)
    abstract fun bindRewardsShopViewModel(viewModel: RewardsShopViewModel): ViewModel
}