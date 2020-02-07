package com.elli0tt.rpg_life.data.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;

public class QuestsSharedPrefUtils {
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREFERENCES_NAME = "quests shared preferences";
    private static final String KEY_QUEST_FILTER_STATE = "key quests filter state";

    public QuestsSharedPrefUtils(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }

    public QuestsFilterState getQuestFilterState() {
        int ordinal = sharedPreferences.getInt(KEY_QUEST_FILTER_STATE,
                QuestsFilterState.ACTIVE.ordinal());
        return QuestsFilterState.values()[ordinal];
    }

    public void setQuestsFilterState(QuestsFilterState filterState) {
        sharedPreferences.edit().putInt(KEY_QUEST_FILTER_STATE, filterState.ordinal()).apply();
    }

}
