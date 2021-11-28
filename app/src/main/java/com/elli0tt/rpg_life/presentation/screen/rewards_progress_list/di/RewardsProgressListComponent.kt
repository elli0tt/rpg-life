package com.elli0tt.rpg_life.presentation.screen.rewards_progress_list.di

import com.elli0tt.rpg_life.presentation.screen.rewards_progress_list.RewardsProgressListFragment
import dagger.Subcomponent

@RewardsProgressListScope
@Subcomponent
interface RewardsProgressListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RewardsProgressListComponent
    }

    fun inject(fragment: RewardsProgressListFragment)
}