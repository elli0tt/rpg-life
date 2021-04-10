package com.elli0tt.rpg_life.presentation.screen.add_category_to_skill

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.adapter.add_category_to_skill.AddCategoryToSkillAdapter
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di.AddCategoryToSkillComponent
import timber.log.Timber
import javax.inject.Inject

class AddCategoryToSkillFragment : BaseFragment(R.layout.fragment_add_category_to_skill) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var addCategoryToSkillComponent: AddCategoryToSkillComponent

    private lateinit var viewModel: AddCategoryToSkillViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var addCategoryToSkillAdapter: AddCategoryToSkillAdapter

    private lateinit var navController: NavController

    private val args: AddCategoryToSkillFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()

        navController = NavHostFragment.findNavController(this)

        recyclerView = view.findViewById(R.id.recycler_view)

        setHasOptionsMenu(true)
        setupRecyclerView()
        subscribeToViewModel()
    }

    private fun initDagger() {
        addCategoryToSkillComponent = appComponent.addCategoryToSkillComponentFactory().create()
        addCategoryToSkillComponent.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }

    private fun setupRecyclerView() {
        addCategoryToSkillAdapter = AddCategoryToSkillAdapter()
        recyclerView.adapter = addCategoryToSkillAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        addCategoryToSkillAdapter.onItemClickListener =
                AddCategoryToSkillAdapter.OnItemClickListener { position ->
                    viewModel.updateCategory(position, args.skillId)
                    navController.popBackStack()
                }
    }

    private fun subscribeToViewModel() {
        viewModel.skillsCategoriesToShow.observe(viewLifecycleOwner) {
            addCategoryToSkillAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_category_to_skill_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_category -> navigateToAddSkillsCategoryScreen()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToAddSkillsCategoryScreen() {
        val action = AddCategoryToSkillFragmentDirections.actionAddCategoryToSkillScreenToAddEditSkillCategoryScreen()
        navController.navigate(action)
    }
}