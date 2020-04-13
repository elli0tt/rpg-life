package com.elli0tt.rpg_life.presentation.quests

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.Quest
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuestsFragment() : Fragment() {
    private var viewModel: QuestsViewModel? = null
    private val questsAdapter = QuestsAdapter()
    private var navController: NavController? = null
    private var fab: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
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
        fab = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.quests_recycler_view)
        subscribeToViewModel()
        setHasOptionsMenu(true)
        setupQuestsRecyclerView()
        fab?.setOnClickListener { v: View? -> navigateToAddQuestScreen() }
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
        if (viewModel!!.quests.value != null) {
            navigateToEditQuestScreen(viewModel!!.quests.value!![position].id)
        }
    }

    private fun setupQuestsRecyclerView() {
        recyclerView!!.adapter = questsAdapter
        questsAdapter.setOnItemClickListener(onItemClickListener)
        questsAdapter.setOnItemLongClickListener { position: Int ->
            val activity: AppCompatActivity? = activity as AppCompatActivity?
            if (activity != null) {
                actionMode = activity.startSupportActionMode(
                        ActionModeController(viewModel, questsAdapter))
            }
            viewModel!!.startSelection()
            questsAdapter.startSelection(position)
        }
        questsAdapter.setOnSelectionFinishedListener {
            if (actionMode != null) {
                actionMode!!.finish()
                actionMode = null
            }
        }

        questsAdapter.setOnIsCompleteCheckBoxClickListener { isCompleted: Boolean, position: Int -> viewModel!!.completeQuest(position, isCompleted) }
        questsAdapter.setOnIsImportantCheckBoxClickListener { isImportant: Boolean, position: Int -> viewModel!!.setQuestImportant(position, isImportant) }
        recyclerView!!.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL
                , false)
        //        recyclerView.addItemDecoration(new DividerItemDecoration(
//                Objects.requireNonNull(getContext()), RecyclerView.VERTICAL));
        recyclerView!!.addItemDecoration(DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL))
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((dy < 0) && !fab!!.isShown && !viewModel!!.isSelectionStarted.value!!) {
                    fab!!.show()
                } else if (dy > 0 && fab!!.isShown) {
                    fab!!.hide()
                }
            }
        })
    }

    private fun subscribeToViewModel() {
        viewModel = ViewModelProviders.of(this).get(QuestsViewModel::class.java)
        viewModel!!.quests.observe(viewLifecycleOwner, Observer { questList: List<Quest?>? -> questsAdapter.submitList(questList) })
        viewModel!!.isSelectionStarted.observe(viewLifecycleOwner, Observer { isSelectionStarted ->
            if (isSelectionStarted) {
                fab!!.hide()
            } else {
                fab!!.show()
            }
        })
        viewModel!!.showCompletedTextResId.observe(viewLifecycleOwner,
                Observer { textResId: Int? ->
                    if (showCompletedMenuItem != null) {
                        showCompletedMenuItem!!.setTitle((textResId)!!)
                    }
                })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.quests_toolbar_menu, menu)
        val populateWithSamplesItem = menu.findItem(R.id.quests_toolbar_menu_populate_with_samples)
        val deleteAllItem = menu.findItem(R.id.quests_toolbar_menu_delete_all)
        showCompletedMenuItem = menu.findItem(R.id.quests_toolbar_menu_show_completed)
        populateWithSamplesItem.isVisible = isToShowDevelopersOptions
        deleteAllItem.isVisible = isToShowDevelopersOptions
        showCompletedMenuItem?.setTitle((viewModel!!.showCompletedTextResId.value)!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.quests_toolbar_menu_delete_all -> {
                viewModel!!.deleteAll()
                fab!!.show()
            }
            R.id.quests_toolbar_menu_populate_with_samples -> viewModel!!.populateWithSamples()
            R.id.quests_toolbar_menu_sort_by_name -> viewModel!!.setSorting(QuestsSortingState.NAME)
            R.id.quests_toolbar_menu_sort_by_date_due -> viewModel!!.setSorting(QuestsSortingState.DATE_DUE)
            R.id.quests_toolbar_menu_sort_by_date_added -> viewModel!!.setSorting(QuestsSortingState.DATE_ADDED)
            R.id.quests_toolbar_menu_sort_by_difficulty -> viewModel!!.setSorting(QuestsSortingState.DIFFICULTY)
            R.id.quests_toolbar_menu_filtering_all -> viewModel!!.setFiltering(QuestsFilterState.ALL)
            R.id.quests_toolbar_menu_filtering_important -> viewModel!!.setFiltering(QuestsFilterState.IMPORTANT)
            R.id.quests_toolbar_menu_filter_today -> viewModel!!.setFiltering(QuestsFilterState.TODAY)
            R.id.quests_toolbar_menu_filter_tomorrow -> viewModel!!.setFiltering(QuestsFilterState.TOMORROW)
            R.id.quests_toolbar_menu_show_completed -> viewModel!!.changeShowCompleted()
        }
        return true
    }

    private fun navigateToAddQuestScreen() {
        val action = QuestsFragmentDirections.actionQuestsScreenToAddQuestScreen()
        navController!!.navigate(action)
    }

    private fun navigateToEditQuestScreen(questId: Int) {
        val action = QuestsFragmentDirections.actionQuestsScreenToEditQuestScreen()
        action.questId = questId
        navController!!.navigate(action)
    }
}