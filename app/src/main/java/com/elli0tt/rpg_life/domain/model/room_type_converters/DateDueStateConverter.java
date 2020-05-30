package com.elli0tt.rpg_life.domain.model.room_type_converters;

import androidx.room.TypeConverter;

import com.elli0tt.rpg_life.domain.model.Quest;

public class DateDueStateConverter {
    @TypeConverter
    public int fromDateDueState(Quest.DateDueCurrentState dateDueCurrentState){
        return dateDueCurrentState.ordinal();
    }

    @TypeConverter
    public Quest.DateDueCurrentState toDateDueState(int ordinal){
        return Quest.DateDueCurrentState.values()[ordinal];
    }
}
