package com.elli0tt.rpg_life.presentation.add_edit_challenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Difficulty
import com.elli0tt.rpg_life.domain.model.Quest
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.quests.FailChallengeUseCase
import com.elli0tt.rpg_life.presentation.add_edit_quest.Constants

class AddEditChallengeViewModel(application: Application) : AndroidViewModel(application) {
    var name = MutableLiveData<String>("")
    var difficulty = MutableLiveData<Difficulty>(Difficulty.NOT_SET)
    var totalDaysCount = MutableLiveData<Int>(0)
    var dayNumber = MutableLiveData<Int>(0)

    var isChallengeNew = true

    var challengeId = 0

    private val failChallengeUseCase: FailChallengeUseCase

    val questsRepository: QuestsRepository = QuestsRepositoryImpl(application)
    val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)

    init {
        failChallengeUseCase = FailChallengeUseCase(skillsRepository, questsRepository)
    }

    fun start(challengeId: Int) {
        if (challengeId != 0) {
            this.challengeId = challengeId
            loadChallenge(challengeId)
            isChallengeNew = false
        }
    }

    private fun loadChallenge(challengeId: Int) {
        object : Thread() {
            override fun run() {
                super.run()
                val challenge = questsRepository.getQuestById(challengeId)
                onChallengeLoaded(challenge)
            }
        }.start()
    }

    private fun onChallengeLoaded(challenge: Quest) {
        name.postValue(challenge.name)
        difficulty.postValue(challenge.difficulty)
        totalDaysCount.postValue(challenge.totalDaysCount)
        dayNumber.postValue(challenge.dayNumber)
    }

    fun save() {
        val challenge = Quest(name.value!!)
        challenge.difficulty = difficulty.value!!
        challenge.isChallenge = true
        challenge.totalDaysCount = totalDaysCount.value!!
        challenge.dayNumber = dayNumber.value!!

        if (isChallengeNew) {
            questsRepository.insertQuests(challenge)
        } else {
            challenge.id = challengeId
            questsRepository.updateQuests(challenge)
        }
    }

    fun changeDifficulty(popUpMenuItemId: Int) {
        when (popUpMenuItemId) {
            Constants.VERY_EASY_POPUP_MENU_ITEM_ID -> difficulty.setValue(Difficulty.VERY_EASY)
            Constants.EASY_POPUP_MENU_ITEM_ID -> difficulty.setValue(Difficulty.EASY)
            Constants.NORMAL_POPUP_MENU_ITEM_ID -> difficulty.setValue(Difficulty.NORMAL)
            Constants.HARD_POPUP_MENU_ITEM_ID -> difficulty.setValue(Difficulty.HARD)
            Constants.VERY_HARD_POPUP_MENU_ITEM_ID -> difficulty.setValue(Difficulty.VERY_HARD)
            Constants.IMPOSSIBLE_POPUP_MENU_ITEM_ID -> difficulty.setValue(Difficulty.IMPOSSIBLE)
        }
    }

    fun failChallenge() {
        failChallengeUseCase.invoke(challengeId, dayNumber.value!!, difficulty.value!!.xpIncrease)
    }

    fun removeDifficulty() {
        difficulty.value = Difficulty.NOT_SET
    }
}