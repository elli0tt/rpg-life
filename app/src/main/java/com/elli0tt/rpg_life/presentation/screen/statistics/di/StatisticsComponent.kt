package com.elli0tt.rpg_life.presentation.screen.statistics.di

import com.elli0tt.rpg_life.presentation.screen.statistics.StatisticsFragment
import dagger.Subcomponent

@StatisticsScope
@Subcomponent
interface StatisticsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): StatisticsComponent
    }

    fun inject(fragment: StatisticsFragment)
}