package com.elli0tt.rpg_life.presentation.screen.add_edit_skill

import android.os.Bundle
import android.view.*
import android.view.View.OnFocusChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.databinding.FragmentAddEditSkillBinding
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill.di.AddEditSkillComponent
import com.elli0tt.rpg_life.presentation.utils.SoftKeyboardUtil
import javax.inject.Inject

class AddEditSkillFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var addEditSkillComponent: AddEditSkillComponent

    private lateinit var viewModel: AddEditSkillViewModel
    private lateinit var navController: NavController

    private val args: AddEditSkillFragmentArgs by navArgs()

    private lateinit var binding: FragmentAddEditSkillBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        initDagger()

        binding = FragmentAddEditSkillBinding.inflate(inflater,
                container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)

        initViews()
        subscribeToViewModel()
    }

    override fun onStart() {
        super.onStart()
        viewModel.start(
                skillId = args.skillId,
                defaultSkillCategoryText = getString(R.string.add_edit_skill_add_category)
        )
    }

    override fun onStop() {
        super.onStop()
        viewModel.save()
    }

    private fun initDagger() {
        addEditSkillComponent = appComponent.addEditSkillComponentFactory().create()
        addEditSkillComponent.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }

    private fun initViews() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setListeners()
    }

    private fun setListeners() {
        binding.nameEditText.onFocusChangeListener = onEditTextsFocusChangeListener
        binding.addCategoryButtonWithIcon.setOnClickListener { navigateToAddCategoryToSkillScreen() }
        binding.addCategoryButtonWithIcon.setOnRemoveClickListener {
            //TODO
        }
    }

    private fun subscribeToViewModel() {
        viewModel.skillsCategoryName.observe(viewLifecycleOwner) {
            binding.addCategoryButtonWithIcon.setText(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_edit_skill_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> navController.popBackStack()
            R.id.delete -> {
                showOnDeleteConfirmDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val onEditTextsFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
        if (!hasFocus) {
            SoftKeyboardUtil.hideKeyboard(v, activity)
        }
    }

    private fun navigateToAddCategoryToSkillScreen() {
        val action = AddEditSkillFragmentDirections.actionAddEditSkillScreenToAddCategoryToSkillFragment()
        action.skillId = viewModel.skillId
        navController.navigate(action)
    }

    private fun showOnDeleteConfirmDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_edit_skill_confirm_delete_title))
            setMessage(getString(R.string.add_edit_skill_confirm_delete_message, viewModel.skillName))
            setCancelable(true)
            setPositiveButton(getString(R.string.add_edit_skill_confirm_positive_text)) { _, _ ->
                viewModel.delete()
                navController.popBackStack()
            }
            setNegativeButton(getString(R.string.add_edit_skill_confirm_negative_text)) { _, _ ->
                //do nothing
            }
            show()
        }
    }
}