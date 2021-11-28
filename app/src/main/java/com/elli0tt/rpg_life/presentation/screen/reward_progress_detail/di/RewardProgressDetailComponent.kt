package com.elli0tt.rpg_life.presentation.screen.reward_progress_detail.di

import com.elli0tt.rpg_life.presentation.screen.reward_progress_detail.RewardProgressDetailFragment
import dagger.Subcomponent

@RewardProgressDetailScope
@Subcomponent
interface RewardProgressDetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RewardProgressDetailComponent
    }

    fun inject(fragment: RewardProgressDetailFragment)
}
