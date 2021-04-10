package com.elli0tt.rpg_life.presentation.core

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.elli0tt.rpg_life.presentation.app.App

abstract class BaseFragment : Fragment {

    protected val app get() = context?.applicationContext as App

    constructor() : super()

    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)
}