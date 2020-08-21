package com.elli0tt.rpg_life.domain.model.room_type_converters;

import androidx.room.TypeConverter;

import com.elli0tt.rpg_life.domain.model.Quest;

public class RepeatStateConverter {
    @TypeConverter
    public Quest.RepeatState toRepeatState(int ordinal) {
        return Quest.RepeatState.values()[ordinal];
    }

    @TypeConverter
    public int fromRepeatState(Quest.RepeatState repeatState) {
        return repeatState.ordinal();
    }
}
