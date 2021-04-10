package com.elli0tt.rpg_life.presentation.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> Fragment.injectViewModel(factory: ViewModelProvider.Factory): T =
        ViewModelProvider(this, factory)[T::class.java]