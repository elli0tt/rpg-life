package com.elli0tt.rpg_life.data.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;
import com.elli0tt.rpg_life.presentation.quests.QuestsSortingState;

public class QuestsSharedPrefUtils {
    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREFERENCES_NAME = "quests shared preferences";
    private static final String KEY_QUESTS_FILTER_STATE = "key quests filter state";
    private static final String KEY_QUESTS_SORTING_STATE = "key quests sorting state";
    private static final String KEY_SHOW_COMPLETED = "key show completed";

    public QuestsSharedPrefUtils(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }

    public QuestsFilterState getQuestsFilterState() {
        int ordinal = sharedPreferences.getInt(KEY_QUESTS_FILTER_STATE,
                QuestsFilterState.ALL.ordinal());
        return QuestsFilterState.values()[ordinal];
    }

    public void setQuestsFilterState(QuestsFilterState filterState) {
        sharedPreferences.edit().putInt(KEY_QUESTS_FILTER_STATE, filterState.ordinal()).apply();
    }

    public QuestsSortingState getQuestsSortingState() {
        int ordinal = sharedPreferences.getInt(KEY_QUESTS_SORTING_STATE,
                QuestsSortingState.NAME.ordinal());
        return QuestsSortingState.values()[ordinal];
    }

    public void setQuestsSortingState(QuestsSortingState sortingState) {
        sharedPreferences.edit().putInt(KEY_QUESTS_SORTING_STATE, sortingState.ordinal()).apply();
    }

    public boolean isShowCompeleted() {
        return sharedPreferences.getBoolean(KEY_SHOW_COMPLETED, false);
    }

    public void setShowCompleted(boolean isShowCompleted) {
        sharedPreferences.edit().putBoolean(KEY_SHOW_COMPLETED, isShowCompleted).apply();
    }

}
