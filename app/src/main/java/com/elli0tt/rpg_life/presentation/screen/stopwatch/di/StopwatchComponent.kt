package com.elli0tt.rpg_life.presentation.screen.stopwatch.di

import com.elli0tt.rpg_life.presentation.screen.stopwatch.StopwatchFragment
import dagger.Subcomponent

@StopwatchScope
@Subcomponent
interface StopwatchComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): StopwatchComponent
    }

    fun inject(fragment: StopwatchFragment)
}