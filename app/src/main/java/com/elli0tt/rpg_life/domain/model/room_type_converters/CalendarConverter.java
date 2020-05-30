package com.elli0tt.rpg_life.domain.model.room_type_converters;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class CalendarConverter {

    @TypeConverter
    public long fromCalendar(Calendar calendar){
        if (calendar != null) {
            return calendar.getTimeInMillis();
        }
        return 0;
    }

    @TypeConverter
    public Calendar toCalendar(long timeInMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return calendar;
    }
}
