package com.elli0tt.rpg_life.domain.use_case.add_edit_quest

import com.elli0tt.rpg_life.domain.constants.Constants
import java.util.*
import javax.inject.Inject

class GetClosestWeekdayCalendarUseCase @Inject constructor() {
    fun invoke(): Calendar {
        val calendar = Calendar.getInstance()
        when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.SATURDAY -> calendar.timeInMillis += Constants.MILLIS_IN_24_HOURS * 2
            Calendar.SUNDAY -> calendar.timeInMillis += Constants.MILLIS_IN_24_HOURS
        }
        return calendar
    }
}