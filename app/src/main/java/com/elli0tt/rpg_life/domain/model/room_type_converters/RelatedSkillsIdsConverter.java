package com.elli0tt.rpg_life.domain.model.room_type_converters;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class RelatedSkillsIdsConverter {
    private static final String DELIMITER = ",";

    @TypeConverter
    public String fromList(List<Integer> ids) {
        StringBuilder result = new StringBuilder();
        for (int id : ids) {
            result.append(id);
            result.append(DELIMITER);
        }
        return result.toString();
    }

    @TypeConverter
    public List<Integer> toList(String ids) {
        List<Integer> resultList = new ArrayList<>();
        String[] stringIds = ids.split(DELIMITER);
        if (isListHasNoIds(stringIds)) {
            return resultList;
        }
        for (String id : ids.split(DELIMITER)) {
            resultList.add(Integer.parseInt(id));
        }
        return resultList;
    }

    private boolean isListHasNoIds(String[] ids) {
        return ids[0].equals("");

    }
}
