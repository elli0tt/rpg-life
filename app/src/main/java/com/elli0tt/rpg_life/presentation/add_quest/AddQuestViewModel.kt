package com.elli0tt.rpg_life.presentation.add_quest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Quest
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.InsertQuestsUseCase

class AddQuestViewModel(application: Application) : AndroidViewModel(application) {

    val name = MutableLiveData<String>()

    private val insertQuestByIdUseCase: InsertQuestsUseCase

    init {
        val questsRepository = QuestsRepositoryImpl(application)

        insertQuestByIdUseCase = InsertQuestsUseCase(questsRepository)
    }

    fun saveQuest() {
        val currentName = name.value
        if (currentName != null){
            insertQuestByIdUseCase.invoke(Quest(currentName))
        }

    }


}