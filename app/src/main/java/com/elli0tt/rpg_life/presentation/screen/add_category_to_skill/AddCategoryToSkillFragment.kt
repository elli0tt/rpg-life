package com.elli0tt.rpg_life.presentation.screen.add_category_to_skill

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.adapter.add_category_to_skill.AddCategoryToSkillAdapter

class AddCategoryToSkillFragment : Fragment() {
    private lateinit var viewModel: AddCategoryToSkillViewModel

    private lateinit var recyclerView: RecyclerView

    private lateinit var addCategoryToSkillAdapter: AddCategoryToSkillAdapter

    private lateinit var navController: NavController

    private val args: AddCategoryToSkillFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_category_to_skill, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddCategoryToSkillViewModel::class.java)

        navController = NavHostFragment.findNavController(this)

        recyclerView = view.findViewById(R.id.recycler_view)

        setHasOptionsMenu(true)
        setupRecyclerView()
        subscribeToViewModel()
    }

    private fun setupRecyclerView() {
        addCategoryToSkillAdapter = AddCategoryToSkillAdapter()
        recyclerView.adapter = addCategoryToSkillAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        addCategoryToSkillAdapter.onItemClickListener = object : AddCategoryToSkillAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                viewModel.updateCategory(position, args.skillId)
                navController.popBackStack()
            }
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