package com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.elli0tt.rpg_life.databinding.FragmentAddEditSkillCategoryBinding
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment

class AddEditSkillsCategoryFragment : BaseFragment() {

    private lateinit var viewModel: AddEditSkillsCategoryViewModel
    private lateinit var binding: FragmentAddEditSkillCategoryBinding

    private val args: AddEditSkillsCategoryFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(AddEditSkillsCategoryViewModel::class.java)

        binding = FragmentAddEditSkillCategoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    private fun subscribeToViewModel() {

    }

    override fun onStart() {
        super.onStart()
        viewModel.start(args.categoryId)
    }

    override fun onStop() {
        super.onStop()
        viewModel.save()
    }
}