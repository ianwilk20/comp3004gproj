package com.example.fooderie;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class OptionsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
