package com.elli0tt.rpg_life.domain.model.room_type_converters;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class SubQuestsListConverter {
    @TypeConverter
    public String fromList(List<Integer> subquestsIds) {
        String result = "";
        if (subquestsIds != null) {
            for (Integer id : subquestsIds) {
                result.concat(id + ",");
            }
        }
        return result;
    }

    @TypeConverter
    public List<Integer> toList(String subquestsIdsString) {
        List<Integer> result = new ArrayList<>();
        for (String id : subquestsIdsString.split(",")) {
            result.add(Integer.getInteger(id));
        }
        return result;
    }
}
