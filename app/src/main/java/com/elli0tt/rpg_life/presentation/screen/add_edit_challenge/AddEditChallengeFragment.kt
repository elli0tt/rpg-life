package com.elli0tt.rpg_life.presentation.screen.add_edit_challenge

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.databinding.FragmentAddEditChallengeBinding
import com.elli0tt.rpg_life.domain.model.Difficulty
import com.elli0tt.rpg_life.domain.model.Quest.DateState
import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.Constants
import com.elli0tt.rpg_life.presentation.utils.SoftKeyboardUtil
import java.util.*

class AddEditChallengeFragment : Fragment() {

    private lateinit var viewModel: AddEditChallengeViewModel
    private lateinit var binding: FragmentAddEditChallengeBinding
    private lateinit var navController: NavController

    private val args: AddEditChallengeFragmentArgs by navArgs()

    private lateinit var veryEasyTitle: String
    private lateinit var easyTitle: String
    private lateinit var normalTitle: String
    private lateinit var hardTitle: String
    private lateinit var veryHardTitle: String
    private lateinit var impossibleTitle: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(AddEditChallengeViewModel::class.java)
        navController = NavHostFragment.findNavController(this)

        binding = FragmentAddEditChallengeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.difficultyView.setOnClickListener { showDifficultyPopupMenu(it) }
        binding.difficultyView.setOnRemoveClickListener {
            viewModel.removeDifficulty()
            SoftKeyboardUtil.hideKeyboard(it, activity)
        }
        binding.addSkillsButton.setOnClickListener { navigateToAddSkillsToQuestScreen() }
        binding.failButton.setOnClickListener { showFailChallengeConfirmDialog() }
        binding.addDateDueView.setOnClickListener(onAddDateDueViewClickListener)
        binding.addDateDueView.setOnRemoveClickListener(onRemoveDateDueViewClickListener)
        binding.addTimeDueView.setOnClickListener(onAddTimeDueViewClickListener)
        binding.addTimeDueView.setOnRemoveClickListener(onRemoveTimeDueViewClickListener)

        subscribeToViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        veryEasyTitle = getString(R.string.add_edit_quest_difficulty_very_easy,
                Difficulty.VERY_EASY.xpIncrease)
        easyTitle = getString(R.string.add_edit_quest_difficulty_easy,
                Difficulty.EASY.xpIncrease)
        normalTitle = getString(R.string.add_edit_quest_difficulty_normal,
                Difficulty.NORMAL.xpIncrease)
        hardTitle = getString(R.string.add_edit_quest_difficulty_hard,
                Difficulty.HARD.xpIncrease)
        veryHardTitle = getString(R.string.add_edit_quest_difficulty_very_hard,
                Difficulty.VERY_HARD.xpIncrease)
        impossibleTitle = getString(R.string.add_edit_quest_difficulty_impossible,
                Difficulty.IMPOSSIBLE.xpIncrease)
    }

    private fun subscribeToViewModel() {
        viewModel.difficulty.observe(viewLifecycleOwner, Observer<Difficulty> { difficulty: Difficulty? ->
            when (difficulty) {
                Difficulty.VERY_EASY -> binding.difficultyView.setText(veryEasyTitle)
                Difficulty.EASY -> binding.difficultyView.setText(easyTitle)
                Difficulty.NORMAL -> binding.difficultyView.setText(normalTitle)
                Difficulty.HARD -> binding.difficultyView.setText(hardTitle)
                Difficulty.VERY_HARD -> binding.difficultyView.setText(veryHardTitle)
                Difficulty.IMPOSSIBLE -> binding.difficultyView.setText(impossibleTitle)
                Difficulty.NOT_SET -> binding.difficultyView.setText(R.string.add_difficulty)
            }
            if (difficulty == Difficulty.NOT_SET) {
                binding.difficultyView.setRemoveIconVisibility(View.INVISIBLE)
            } else {
                binding.difficultyView.setRemoveIconVisibility(View.VISIBLE)
            }
        })

        viewModel.dateDueState.observe(viewLifecycleOwner,
                Observer { dateDueState: DateState? ->
                    when (dateDueState) {
                        DateState.NOT_SET -> {
                            binding.addDateDueView.setText(R.string.add_edit_quest_add_date_due)
                            binding.addDateDueView.setRemoveIconVisibility(View.INVISIBLE)
                            binding.addTimeDueView.visibility = View.GONE
                            binding.addTimeDueView.setRemoveIconVisibility(View.INVISIBLE)
                        }
                        DateState.DATE_SET -> {
                            binding.addDateDueView.setText(viewModel.getDateDueFormatted())
                            binding.addDateDueView.setRemoveIconVisibility(View.VISIBLE)
                            binding.addTimeDueView.visibility = View.VISIBLE
                            binding.addTimeDueView.setText(R.string.add_edit_quest_add_time_due)
                        }
                        DateState.DATE_TIME_SET -> {
                            binding.addDateDueView.setText(viewModel.getDateDueFormatted())
                            binding.addDateDueView.setRemoveIconVisibility(View.VISIBLE)
                            binding.addTimeDueView.visibility = View.VISIBLE
                            binding.addTimeDueView.setText(viewModel.getTimeDueFormatted())
                            binding.addTimeDueView.setRemoveIconVisibility(View.VISIBLE)
                        }
                    }
                })
    }

    override fun onStart() {
        super.onStart()
        viewModel.start(args.challengeId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_edit_challenge_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> navController.popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        viewModel.save()
    }

    private fun showDifficultyPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        val menu = popupMenu.menu
        menu.add(Menu.NONE, Constants.VERY_EASY_POPUP_MENU_ITEM_ID,
                Constants.VERY_EASY_POPUP_MENU_ITEM_ORDER, veryEasyTitle)
        menu.add(Menu.NONE, Constants.EASY_POPUP_MENU_ITEM_ID, Constants.EASY_POPUP_MENU_ITEM_ORDER,
                easyTitle)
        menu.add(Menu.NONE, Constants.NORMAL_POPUP_MENU_ITEM_ID,
                Constants.NORMAL_POPUP_MENU_ITEM_ORDER, normalTitle)
        menu.add(Menu.NONE, Constants.HARD_POPUP_MENU_ITEM_ID, Constants.HARD_POPUP_MENU_ITEM_ORDER,
                hardTitle)
        menu.add(Menu.NONE, Constants.VERY_HARD_POPUP_MENU_ITEM_ID,
                Constants.VERY_HARD_POPUP_MENU_ITEM_ORDER, veryHardTitle)
        menu.add(Menu.NONE, Constants.IMPOSSIBLE_POPUP_MENU_ITEM_ID,
                Constants.IMPOSSIBLE_POPUP_MENU_ITEM_ORDER, impossibleTitle)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            binding.difficultyView.setText(item.title.toString())
            viewModel.changeDifficulty(item.itemId)
            true
        }
        popupMenu.show()
    }

    private fun showFailChallengeConfirmDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.add_edit_challege_fail_confirm_title))
            setMessage(getString(R.string.add_edit_challenge_fail_confirm_message))
            setCancelable(true)
            setPositiveButton(getString(R.string.add_edit_challenge_fail_confrim_positive)) { _, _ ->
                viewModel.failChallenge()
                navController.popBackStack()
            }
            setNegativeButton(getString(R.string.add_edit_challenge_fail_confirm_negative)) { _, _ ->
                // do nothing
            }
            show()
        }
    }

    private fun navigateToAddSkillsToQuestScreen() {
        val action = AddEditChallengeFragmentDirections.actionAddEditChallengeScreenToAddSkillsToQuestScreen()
        action.questId = viewModel.challengeId
        navController.navigate(action)
    }

    private val onDateDueSetListener = OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
        viewModel.setDateDue(year, month, dayOfMonth)
    }

    private val onTimeDueSetListener = OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
        viewModel.setDateDue(hourOfDay, minute)
    }

    private val onAddDateDueViewClickListener = View.OnClickListener { v: View? ->
        SoftKeyboardUtil.hideKeyboard(v, activity)
        showAddDateDuePopupMenu(v!!)
    }

    private val onRemoveDateDueViewClickListener = View.OnClickListener { v ->
        SoftKeyboardUtil.hideKeyboard(v, activity)
        viewModel.removeDateDue()
    }

    private val onAddTimeDueViewClickListener = View.OnClickListener { v: View? ->
        SoftKeyboardUtil.hideKeyboard(v, activity)
        pickTime(onTimeDueSetListener)
    }

    private val onRemoveTimeDueViewClickListener = View.OnClickListener { v: View? ->
        SoftKeyboardUtil.hideKeyboard(v, activity)
        viewModel.removeTimeDue()
    }

    private fun pickDate(onDateSetListener: OnDateSetListener) {
        val datePickerDialog = DatePickerDialog(requireContext(),
                onDateSetListener,
                Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH],
                Calendar.getInstance()[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }

    private fun pickTime(onTimeSetListener: OnTimeSetListener) {
        val timePickerDialog = TimePickerDialog(context, onTimeSetListener,
                Calendar.getInstance()[Calendar.HOUR_OF_DAY],
                Calendar.getInstance()[Calendar.MINUTE],
                true)
        timePickerDialog.show()
    }

    private fun showAddDateDuePopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.add_edit_quest_add_date_due_popup_menu,
                popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.add_edit_quest_add_date_due_popup_today -> {
                    viewModel.setDateDueToday()
                    return@setOnMenuItemClickListener true
                }
                R.id.add_edit_quest_add_date_due_popup_tomorrow -> {
                    viewModel.setDateDueTomorrow()
                    return@setOnMenuItemClickListener true
                }
                R.id.add_edit_quest_add_date_due_popup_next_week -> {
                    viewModel.setDateDueNextWeek()
                    return@setOnMenuItemClickListener true
                }
                R.id.add_edit_quest_add_date_due_popup_pick_date -> {
                    pickDate(onDateDueSetListener)
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
        popupMenu.show()
    }
}