package com.elli0tt.rpg_life.domain.model.room_type_converters

import androidx.room.TypeConverter
import com.elli0tt.rpg_life.domain.model.Difficulty
import com.elli0tt.rpg_life.domain.model.Quest

class DifficultyConverter {
    @TypeConverter
    fun fromEnum(difficulty: Difficulty): Int {
        return difficulty.ordinal
    }

    @TypeConverter
    fun toEnum(difficultyOrdinal: Int): Difficulty {
        return Difficulty.values()[difficultyOrdinal]
    }
}