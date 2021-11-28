package com.elli0tt.rpg_life.presentation.screen.rewards_list.di

import com.elli0tt.rpg_life.presentation.screen.rewards_list.RewardsListFragment
import dagger.Subcomponent

@RewardsListScope
@Subcomponent
interface RewardsListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RewardsListComponent
    }

    fun inject(fragment: RewardsListFragment)
}