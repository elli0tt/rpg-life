package com.elli0tt.rpg_life.presentation.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.elli0tt.rpg_life.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_fragment, rootKey);
    }


}
