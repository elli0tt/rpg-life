package com.elli0tt.rpg_life.presentation.screen.add_edit_quest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.elli0tt.rpg_life.domain.model.AddSkillData

class AddEditQuestAddSkillToQuestSharedViewModel(application: Application): AndroidViewModel(application) {
    var relatedSkills = MediatorLiveData<MutableList<AddSkillData>>()
}