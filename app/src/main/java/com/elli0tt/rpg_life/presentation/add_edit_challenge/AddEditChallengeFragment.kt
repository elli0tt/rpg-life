package com.elli0tt.rpg_life.presentation.add_edit_challenge

import android.os.Bundle
import android.view.*
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
import com.elli0tt.rpg_life.presentation.add_edit_quest.Constants
import com.elli0tt.rpg_life.presentation.custom_view.ButtonWithRemoveIcon
import com.elli0tt.rpg_life.presentation.utils.SoftKeyboardUtil

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
        binding.failButton.setOnClickListener { viewModel.failChallenge() }

        subscribeToViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
    }

    override fun onStart() {
        super.onStart()
        viewModel.start(args.challengeId)
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

    private fun navigateToAddSkillsToQuestScreen() {
        val action = AddEditChallengeFragmentDirections.actionAddEditChallengeScreenToAddSkillsToQuestScreen()
        action.questId = viewModel.challengeId
        navController.navigate(action)
    }
}