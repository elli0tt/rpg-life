package com.elli0tt.rpg_life.domain.model.room_type_converters;

import androidx.room.TypeConverter;

import com.elli0tt.rpg_life.domain.model.Quest;

public class DateDueStateConverter {
    @TypeConverter
    public int fromDateDueState(Quest.DateDueState dateDueState){
        return dateDueState.ordinal();
    }

    @TypeConverter
    public Quest.DateDueState toDateDueState(int ordinal){
        return Quest.DateDueState.values()[ordinal];
    }
}
