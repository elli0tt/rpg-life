package com.elli0tt.rpg_life.di

import com.elli0tt.rpg_life.di.modules.AppModule
import com.elli0tt.rpg_life.di.modules.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RepositoryModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create()
    }
}