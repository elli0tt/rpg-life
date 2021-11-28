package com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic

import android.os.Bundle
import android.view.View
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.di.AddEditCharacteristicComponent

class AddEditCharacteristicFragment : BaseFragment(R.layout.fragment_add_edit_characteristic) {

    private lateinit var addEditCharacteristicComponent: AddEditCharacteristicComponent

    private lateinit var viewModel: AddEditCharacteristicViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()
    }

    private fun initDagger() {
        addEditCharacteristicComponent =
            appComponent.addEditCharacteristicComponentFactory().create()
        addEditCharacteristicComponent.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }
}