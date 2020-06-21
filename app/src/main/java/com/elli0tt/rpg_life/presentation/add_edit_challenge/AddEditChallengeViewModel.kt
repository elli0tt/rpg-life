package com.elli0tt.rpg_life.presentation.add_edit_challenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Difficulty
import com.elli0tt.rpg_life.domain.model.Quest
import com.elli0tt.rpg_life.domain.model.Quest.DateState
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.*
import com.elli0tt.rpg_life.domain.use_case.quests.FailChallengeUseCase
import com.elli0tt.rpg_life.presentation.add_edit_quest.Constants
import java.text.DateFormat
import java.util.*

class AddEditChallengeViewModel(application: Application) : AndroidViewModel(application) {
    var name = MutableLiveData("")
    var difficulty = MutableLiveData(Difficulty.NOT_SET)
    var totalDaysCount = MutableLiveData(0)
    var dayNumber = MutableLiveData(0)

    var isChallengeNew = true

    var challengeId = 0

    private val _dateDueState = MutableLiveData(DateState.NOT_SET)
    val dateDueState: LiveData<DateState> = _dateDueState

    private var dateDue = Calendar.getInstance()

    private val dateFormat = DateFormat.getDateInstance(DateFormat.LONG)
    private val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    private val failChallengeUseCase: FailChallengeUseCase

    val questsRepository: QuestsRepository = QuestsRepositoryImpl(application)
    val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)

    private val today: String = application.getString(R.string.quest_date_due_today)
    private val tomorrow: String = application.getString(R.string.quest_date_due_tomorrow)

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
        _dateDueState.postValue(challenge.dateDueState)
        dateDue = challenge.dateDue
    }

    fun save() {
        val challenge = Quest(name = name.value!!)
        challenge.difficulty = difficulty.value!!
        challenge.isChallenge = true
        challenge.totalDaysCount = totalDaysCount.value!!
        challenge.dayNumber = dayNumber.value!!
        challenge.dateDue = dateDue
        challenge.dateDueState = _dateDueState.value!!

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

    fun getDateDueFormatted(): String {
        if (IsCalendarEqualsTodayCalendarUseCase().invoke(dateDue)) {
            return today
        }
        return if (IsCalendarEqualsTomorrowCalendarUseCase().invoke(dateDue)) {
            tomorrow
        } else dateFormat.format(dateDue.time)
    }

    fun getTimeDueFormatted(): String {
        return timeFormat.format(dateDue.time)
    }

    fun setDateDue(year: Int, month: Int, dayOfMonth: Int) {
        dateDue[Calendar.YEAR] = year
        dateDue[Calendar.MONTH] = month
        dateDue[Calendar.DAY_OF_MONTH] = dayOfMonth
        _dateDueState.value = DateState.DATE_SET
    }

    fun setDateDue(hourOfDay: Int, minutes: Int) {
        dateDue[Calendar.HOUR_OF_DAY] = hourOfDay
        dateDue[Calendar.MINUTE] = minutes
        _dateDueState.value = DateState.DATE_TIME_SET
    }

    fun removeDateDue() {
        _dateDueState.value = DateState.NOT_SET
    }

    fun removeTimeDue() {
        _dateDueState.value = DateState.DATE_SET
    }

    fun setDateDueToday() {
        dateDue = GetTodayCalendarUseCase().invoke()
        _dateDueState.value = DateState.DATE_SET
    }

    fun setDateDueTomorrow() {
        dateDue = GetTomorrowCalendarUseCase().invoke()
        _dateDueState.value = DateState.DATE_SET
    }

    fun setDateDueNextWeek() {
        dateDue = GetNextWeekCalendarUseCase().invoke()
        _dateDueState.value = DateState.DATE_SET
    }

    fun setDateDueClosestWeekday() {
        dateDue = GetClosestWeekdayCalendarUseCase().invoke()
        _dateDueState.value = DateState.DATE_SET
    }
}