package com.elli0tt.rpg_life.presentation.screen.rewards_progress_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.domain.constants.Constants
import com.elli0tt.rpg_life.domain.model.RewardProgress
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RewardsProgressListViewModel @Inject constructor() : ViewModel() {

    private var _rewardsList = MutableLiveData<List<RewardProgress>>()
    val rewardsList: LiveData<List<RewardProgress>> = _rewardsList

    private val realRewardsList = listOf(
        RewardProgress(
            name = "+1 глава книги в неделю",
            startTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.NOVEMBER, date = 21),
            endTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.NOVEMBER, date = 28)
        ),
        RewardProgress(
            name = "+1 серия сериала в день",
            startTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.NOVEMBER, date = 28),
            endTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.DECEMBER, date = 5)
        ),
        RewardProgress(
            name = "+1 фильм в неделю",
            startTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.DECEMBER, date = 5),
            endTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.DECEMBER, date = 12)
        ),
        RewardProgress(
            name = "+1 глава книги в день",
            startTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.DECEMBER, date = 12),
            endTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.DECEMBER, date = 26)
        ),
        RewardProgress(
            name = "+1 глава книги в день",
            startTimeInMillis = getTimeInMillis(year = 2021, month = Calendar.DECEMBER, date = 26),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.JANUARY, date = 9)
        ),
        RewardProgress(
            name = "+30 минут игры в компьютерные игры",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.JANUARY, date = 9),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.JANUARY, date = 23)
        ),
        RewardProgress(
            name = "+30 минут игры в компьютерные игры",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.JANUARY, date = 23),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.FEBRUARY, date = 6)
        ),
        RewardProgress(
            name = "+1 глава аудиокниги",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.FEBRUARY, date = 6),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.FEBRUARY, date = 20)
        ),
        RewardProgress(
            name = "+1 глава аудиокниги",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.FEBRUARY, date = 20),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.MARCH, date = 6)
        ),
        RewardProgress(
            name = "+1 партия в шахматы в день",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.MARCH, date = 6),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.MARCH, date = 20)
        ),
        RewardProgress(
            name = "+1 партия в шахматы в день",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.MARCH, date = 20),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.APRIL, date = 3)
        ),
        RewardProgress(
            name = "+1 фильм в неделю",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.APRIL, date = 3),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.APRIL, date = 17)
        ),
        RewardProgress(
            name = "+1 подкаст в день",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.APRIL, date = 17),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.MAY, date = 1)
        ),
        RewardProgress(
            name = "+1 подкаст в день",
            startTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.MAY, date = 1),
            endTimeInMillis = getTimeInMillis(year = 2022, month = Calendar.MAY, date = 8)
        )
    )

    init {
        loadData()
    }

    private fun getTimeInMillis(year: Int, month: Int, date: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, date, 0, 0, 0)
        return calendar.timeInMillis
    }

    private fun loadData() {
        _rewardsList.value = realRewardsList
    }

    private fun generateMockRewardsSize(): List<RewardProgress> {
        val size = 50
        val result = ArrayList<RewardProgress>(size)

        val november30 = Calendar.getInstance()
        november30.set(2021, Calendar.NOVEMBER, 29, 0, 0, 0)
        result.add(
            RewardProgress(
                name = "+1 глава книги в день",
                startTimeInMillis = Calendar.getInstance().timeInMillis - Constants.MILLIS_IN_24_HOURS,
                endTimeInMillis = november30.timeInMillis
            )
        )
        result.add(
            RewardProgress(
                name = "+1 глава книги в день",
                startTimeInMillis = Calendar.getInstance().timeInMillis - Constants.MILLIS_IN_24_HOURS * 2,
                endTimeInMillis = Calendar.getInstance().timeInMillis - Constants.MILLIS_IN_24_HOURS
            )
        )
        repeat(size) {
            result.add(
                RewardProgress(
                    name = "+1 глава книги в день",
                    startTimeInMillis = Calendar.getInstance().timeInMillis,
                    endTimeInMillis = Calendar.getInstance().timeInMillis + Constants.MILLIS_IN_24_HOURS
                )
            )
        }

        return result
    }
}