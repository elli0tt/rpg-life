package com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.adapter.add_skill_to_quest.AddSkillsToQuestAdapter
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest.di.AddSkillsToQuestComponent

class AddSkillsToQuestFragment : BaseFragment(R.layout.fragment_add_skills_to_quest) {

    private lateinit var addSkillsToQuestComponent: AddSkillsToQuestComponent

    private lateinit var viewModel: AddSkillsToQuestViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var addSkillsToQuestAdapter: AddSkillsToQuestAdapter

    private val args: AddSkillsToQuestFragmentArgs by navArgs()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()

        navController = NavHostFragment.findNavController(this)

        initViews(view)
        subscribeToViewModel()

        viewModel.start(args.questId)
    }

    override fun onStop() {
        super.onStop()
        viewModel.save()
    }

    private fun initDagger() {
        addSkillsToQuestComponent = appComponent.addSkillsToQuestComponentFactory().create()
        addSkillsToQuestComponent.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recycler)

        setupRecyclerView()
        setupToolbar()
    }

    private fun setupRecyclerView() {
        addSkillsToQuestAdapter = AddSkillsToQuestAdapter()
        recyclerView.adapter = addSkillsToQuestAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        addSkillsToQuestAdapter.onXpPercentageSeekBarTouchStopListener =
            object : AddSkillsToQuestAdapter.OnXpPercentageSeekBarTouchStopListener {
                override fun onTouchStop(position: Int, xpPercentage: Int) {
                    viewModel.onXpPercentageSeekBarTouchStop(position, xpPercentage)
                }
            }
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun subscribeToViewModel() {
        viewModel.skillsToShow.observe(viewLifecycleOwner) { allSkillsData ->
            addSkillsToQuestAdapter.submitList(allSkillsData)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_skills_to_quest_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> popUpToQuestsScreen()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun popUpToQuestsScreen() {
        val action = AddSkillsToQuestFragmentDirections.actionAddSkillsToQuestScreenToQuestsScreen()
        navController.navigate(action)
    }
}