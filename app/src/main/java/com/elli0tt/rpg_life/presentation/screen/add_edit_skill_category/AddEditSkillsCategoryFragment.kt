package com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.elli0tt.rpg_life.databinding.FragmentAddEditSkillCategoryBinding

class AddEditSkillsCategoryFragment : Fragment() {
    private lateinit var viewModel: AddEditSkillsCategoryViewModel
    private lateinit var binding: FragmentAddEditSkillCategoryBinding

    private val args: AddEditSkillsCategoryFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(AddEditSkillsCategoryViewModel::class.java)

        binding = FragmentAddEditSkillCategoryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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