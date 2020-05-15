package com.elli0tt.rpg_life.presentation.quests

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.Quest
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuestsFragment : Fragment() {
    private lateinit var viewModel: QuestsViewModel
    private val questsAdapter = QuestsAdapter()
    private lateinit var navController: NavController
    private lateinit var addQuestFab: FloatingActionButton
    private lateinit var addChallengeFab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private var actionMode: ActionMode? = null
    private var isToShowDevelopersOptions = false
    private var showCompletedMenuItem: MenuItem? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)

        addQuestFab = view.findViewById(R.id.add_quest_fab)
        addChallengeFab = view.findViewById(R.id.add_challenge_fab)
        recyclerView = view.findViewById(R.id.quests_recycler_view)

        subscribeToViewModel()
        setHasOptionsMenu(true)
        setupQuestsRecyclerView()

        addQuestFab.setOnClickListener { navigateToAddQuestScreen() }
        addChallengeFab.setOnClickListener { navigateToAddChallengeScreen() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        questsAdapter.notifyDataSetChanged()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        isToShowDevelopersOptions = sharedPreferences.getBoolean("developers_options", false)
        if (activity != null) {
            requireActivity().invalidateOptionsMenu()
        }
    }

    private val onItemClickListener = QuestsAdapter.OnItemClickListener { position: Int ->
        val quests = viewModel.quests.value
        if (quests != null) {
            if (quests[position].isChallenge) {
                navigateToEditChallengeScreen(quests[position].id)
            } else {
                navigateToEditQuestScreen(viewModel.quests.value!![position].id)
            }
        }
    }

    private fun setupQuestsRecyclerView() {
        recyclerView.adapter = questsAdapter
        questsAdapter.setOnItemClickListener(onItemClickListener)
        questsAdapter.setOnItemLongClickListener { position: Int ->
            val activity: AppCompatActivity? = activity as AppCompatActivity?
            if (activity != null) {
                actionMode = activity.startSupportActionMode(
                        ActionModeController(viewModel, questsAdapter))
            }
            viewModel.startSelection()
            questsAdapter.startSelection(position)
        }
        questsAdapter.setOnSelectionFinishedListener {
            if (actionMode != null) {
                actionMode!!.finish()
                actionMode = null
            }
        }

        questsAdapter.setOnIsCompleteCheckBoxClickListener { isCompleted: Boolean, position: Int ->
            viewModel.completeQuest(position, isCompleted)
        }
        questsAdapter.setOnIsImportantCheckBoxClickListener { isImportant: Boolean, position: Int ->
            viewModel.setQuestImportant(position, isImportant)
        }
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((dy < 0) && !addQuestFab.isShown && !viewModel.isSelectionStarted.value!!) {
                    addQuestFab.show()
                } else if (dy > 0 && addQuestFab.isShown) {
                    addQuestFab.hide()
                }
            }
        })
    }

    private fun subscribeToViewModel() {
        viewModel = ViewModelProvider(this).get(QuestsViewModel::class.java)
        viewModel.quests.observe(viewLifecycleOwner, Observer { questList: List<Quest?>? -> questsAdapter.submitList(questList) })
        viewModel.isSelectionStarted.observe(viewLifecycleOwner, Observer { isSelectionStarted ->
            if (isSelectionStarted) {
                addQuestFab.hide()
            } else {
                addQuestFab.show()
            }
        })
        viewModel.showCompletedTextResId.observe(viewLifecycleOwner,
                Observer { textResId: Int? ->
                    if (showCompletedMenuItem != null) {
                        showCompletedMenuItem!!.setTitle((textResId)!!)
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quests_toolbar_menu, menu)
        val populateWithSamplesItem = menu.findItem(R.id.populate_with_samples)
        val deleteAllItem = menu.findItem(R.id.quests_toolbar_menu_delete_all)
        showCompletedMenuItem = menu.findItem(R.id.show_completed)
        populateWithSamplesItem.isVisible = isToShowDevelopersOptions
        deleteAllItem.isVisible = isToShowDevelopersOptions
        showCompletedMenuItem?.setTitle((viewModel.showCompletedTextResId.value)!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.quests_toolbar_menu_delete_all -> {
                viewModel.deleteAll()
                addQuestFab.show()
            }
            R.id.populate_with_samples -> viewModel.populateWithSamples()
            R.id.show_completed -> viewModel.changeShowCompleted()

            R.id.sort_by_name -> viewModel.setSorting(QuestsSortingState.NAME)
            R.id.sort_by_date_due -> viewModel.setSorting(QuestsSortingState.DATE_DUE)
            R.id.sort_by_date_added -> viewModel.setSorting(QuestsSortingState.DATE_ADDED)
            R.id.sort_by_difficulty -> viewModel.setSorting(QuestsSortingState.DIFFICULTY)

            R.id.filtering_all -> viewModel.setFiltering(QuestsFilterState.ALL)
            R.id.filtering_important -> viewModel.setFiltering(QuestsFilterState.IMPORTANT)
            R.id.filter_today -> viewModel.setFiltering(QuestsFilterState.TODAY)
            R.id.filter_tomorrow -> viewModel.setFiltering(QuestsFilterState.TOMORROW)
        }
        return true
    }

    private fun navigateToAddQuestScreen() {
        val action = QuestsFragmentDirections.actionQuestsScreenToAddQuestScreen()
        navController.navigate(action)
    }

    private fun navigateToEditQuestScreen(questId: Int) {
        val action = QuestsFragmentDirections.actionQuestsScreenToEditQuestScreen()
        action.questId = questId
        navController.navigate(action)
    }

    private fun navigateToAddChallengeScreen() {
        val action = QuestsFragmentDirections.actionQuestsScreenToAddEditChallengeScreen()
        navController.navigate(action)
    }

    private fun navigateToEditChallengeScreen(challengeId: Int) {
        val action = QuestsFragmentDirections.actionQuestsScreenToAddEditChallengeScreen()
        action.challengeId = challengeId
        navController.navigate(action)
    }
}