package com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import javax.inject.Inject

class AddEditCharacteristicFragment : BaseFragment(R.layout.fragment_add_edit_characteristic) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddEditCharacteristicViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()
    }

    private fun initDagger() {
        val component = appComponent.addEditCharacteristicComponentFactory().create()
        component.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }
}