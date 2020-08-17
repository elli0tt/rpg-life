package com.elli0tt.rpg_life.data.shared_prefs

import android.content.Context

class SharedPreferencesUtils(context: Context) {

    private val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    private companion object {
        const val SHARED_PREFERENCES_NAME = "com.elli0tt.rpg_life.data.shared_prefs.SharedPreferencesUtils"

        const val KEY_CHARACTER_COINS = "KEY_CHARACTER_COINS"

        const val CHARACTER_COINS_DEFAULT = 0
    }

    var characterCoins: Int
        get() = sharedPreferences.getInt(KEY_CHARACTER_COINS, CHARACTER_COINS_DEFAULT)
        set(value) = sharedPreferences.edit().putInt(KEY_CHARACTER_COINS, value).apply()
}