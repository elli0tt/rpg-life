package com.elli0tt.rpg_life.presentation.screen.countdown_timer.di

import com.elli0tt.rpg_life.presentation.screen.countdown_timer.CountDownFragment
import dagger.Subcomponent

@CountDownScope
@Subcomponent
interface CountDownComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CountDownComponent
    }

    fun inject(fragment: CountDownFragment)
}