package com.elli0tt.rpg_life.presentation.add_skills_to_quest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R

class AddSkillsToQuestFragment : Fragment() {
    private lateinit var viewModel: AddSkillsToQuestViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var addSkillsToQuestAdapter: AddSkillsToQuestAdapter

    private val args: AddSkillsToQuestFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(AddSkillsToQuestViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_skills_to_quest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.add_skills_to_quest_recycler)

        setupRecyclerView()
        subscribeToViewModel()

        viewModel.start(args.questId)
    }

    private fun setupRecyclerView() {
        addSkillsToQuestAdapter = AddSkillsToQuestAdapter()
        recyclerView.adapter = addSkillsToQuestAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        addSkillsToQuestAdapter.onSelectCheckBoxClickListener = object : AddSkillsToQuestAdapter.OnSelectCheckBoxClickListener{
            override fun onCheck(position: Int, isChecked: Boolean) {
                viewModel.onSelectCheckBoxCheckChange(position, isChecked)
            }
        }
    }

    private fun subscribeToViewModel() {
        viewModel.skillsToShow.observe(viewLifecycleOwner, Observer { allSkillsData ->
            addSkillsToQuestAdapter.submitList(allSkillsData)
        })
    }

    override fun onResume() {
        super.onResume()
        addSkillsToQuestAdapter.notifyDataSetChanged()
    }
}