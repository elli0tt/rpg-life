package com.elli0tt.rpg_life.presentation.screen.rewards_shop.di

import com.elli0tt.rpg_life.presentation.screen.rewards_shop.RewardsShopFragment
import dagger.Subcomponent

@RewardsShopScope
@Subcomponent
interface RewardsShopComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RewardsShopComponent
    }

    fun inject(fragment: RewardsShopFragment)
}