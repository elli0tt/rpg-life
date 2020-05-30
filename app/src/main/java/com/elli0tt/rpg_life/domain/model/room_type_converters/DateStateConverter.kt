package com.elli0tt.rpg_life.domain.model.room_type_converters

import androidx.room.TypeConverter
import com.elli0tt.rpg_life.domain.model.Quest

class DateStateConverter {
    @TypeConverter
    fun fromEnum(dateState: Quest.DateState): Int {
        return dateState.ordinal
    }

    @TypeConverter
    fun toEnum(ordinal: Int): Quest.DateState {
        return Quest.DateState.values()[ordinal]
    }
}