package com.elli0tt.rpg_life.presentation.quests

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
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
import androidx.work.WorkInfo
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.Quest
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuestsFragment : Fragment() {
    private lateinit var viewModel: QuestsViewModel
    private val questsAdapter = QuestsAdapter()

    private lateinit var navController: NavController
    private lateinit var addQuestFab: FloatingActionButton
    private lateinit var addChallengeFab: FloatingActionButton
    private lateinit var mainFab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabMenuBackgroundView: View
    private lateinit var addChallengeCardView: View
    private lateinit var addQuestCardView: View

    private var actionMode: ActionMode? = null
    private var isToShowDevelopersOptions = false
    private var showCompletedMenuItem: MenuItem? = null

    private var isFabMenuOpened = false

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
        mainFab = view.findViewById(R.id.main_fab)
        recyclerView = view.findViewById(R.id.quests_recycler_view)
        fabMenuBackgroundView = view.findViewById(R.id.fab_menu_background_view)
        addChallengeCardView = view.findViewById(R.id.add_challenge_card_view)
        addQuestCardView = view.findViewById(R.id.add_quest_card_view)

        subscribeToViewModel()
        setHasOptionsMenu(true)
        setupQuestsRecyclerView()

        addQuestFab.setOnClickListener {
            hideFabMenu()

            viewModel.insertEmptyQuest()
        }
        addChallengeFab.setOnClickListener {
            hideFabMenu()
            navigateToAddChallengeScreen()
        }
        mainFab.setOnClickListener {
            if (!isFabMenuOpened) {
                showFabMenu()
            } else {
                hideFabMenu()
            }
        }
        fabMenuBackgroundView.setOnClickListener {
            hideFabMenu()
        }
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
                if ((dy < 0) && !mainFab.isShown && !viewModel.isSelectionStarted.value!!) {
                    mainFab.show()
                } else if (dy > 0 && addQuestFab.isShown) {
                    mainFab.hide()
                }
            }
        })
    }

    private fun subscribeToViewModel() {
        viewModel = ViewModelProvider(this).get(QuestsViewModel::class.java)
        viewModel.quests.observe(viewLifecycleOwner, Observer { questList: List<Quest?>? -> questsAdapter.submitList(questList) })
        viewModel.isSelectionStarted.observe(viewLifecycleOwner, Observer { isSelectionStarted ->
            if (isSelectionStarted) {
                mainFab.hide()
            } else {
                mainFab.show()
            }
        })
        viewModel.showCompletedTextResId.observe(viewLifecycleOwner, Observer { textResId: Int? ->
            if (showCompletedMenuItem != null) {
                showCompletedMenuItem!!.setTitle((textResId)!!)
            }
        })
        viewModel.workInfo.observe(viewLifecycleOwner, workInfoObserver)
    }

    private val workInfoObserver = object : Observer<WorkInfo>{
        override fun onChanged(workInfo: WorkInfo?) {
            if (workInfo != null && workInfo.state.isFinished){
                navigateToEditQuestScreen(workInfo.outputData.getInt(Constants.KEY_QUEST_ID, 0))
                viewModel.updateInsertWorkRequest()
            }
        }
    }

    private fun showFabMenu() {
        isFabMenuOpened = true
        fabMenuBackgroundView.visibility = View.VISIBLE
        addQuestFab.show()
        addChallengeFab.show()

        mainFab.animate().rotation(135f)
        fabMenuBackgroundView.animate().alpha(1f)
        addChallengeFab.animate()
                .translationY(-requireContext().resources.getDimension(R.dimen.add_challenge_fab_translationY))
                .rotation(0f)
        addQuestFab.animate()
                .translationY(-requireContext().resources.getDimension(R.dimen.add_quest_fab_translationY))
                .rotation(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        addQuestCardView.visibility = View.VISIBLE
                        addChallengeCardView.visibility = View.VISIBLE
                    }
                })
    }

    private fun hideFabMenu() {
        isFabMenuOpened = false
        mainFab.animate().rotation(0f)
        fabMenuBackgroundView.animate().alpha(0f)
        addChallengeFab.animate()
                .translationY(0f)
                .rotation(90f)
        addQuestFab.animate()
                .translationY(0f)
                .rotation(90f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        addQuestCardView.visibility = View.GONE
                        addChallengeCardView.visibility = View.GONE
                        addChallengeFab.hide()
                        addQuestFab.hide()
                        fabMenuBackgroundView.visibility = View.GONE
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