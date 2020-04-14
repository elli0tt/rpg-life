package com.elli0tt.rpg_life.presentation.add_edit_skill

import android.os.Bundle
import android.view.*
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.databinding.FragmentAddEditSkillBinding
import com.elli0tt.rpg_life.presentation.utils.SoftKeyboardUtil

class AddEditSkillFragment : Fragment() {
    private lateinit var viewModel: AddEditSkillViewModel
    private lateinit var navController: NavController
    private lateinit var nameEditText: EditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(AddEditSkillViewModel::class.java)
        val binding = FragmentAddEditSkillBinding.inflate(inflater,
                container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
        nameEditText = view.findViewById(R.id.name_edit_text)
        nameEditText.onFocusChangeListener = onEditTextsFocusChangeListener
        subscribeToViewModel()
        setHasOptionsMenu(true)
    }

    private val onEditTextsFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
        if (!hasFocus) {
            SoftKeyboardUtil.hideKeyboard(v, activity)
        }
    }

    private fun subscribeToViewModel() {}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_edit_skill_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_edit_skill_menu_save -> {
                viewModel.saveSkill()
                navController.popBackStack()
                return true
            }
        }
        return false
    }
}