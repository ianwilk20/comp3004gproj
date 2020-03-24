package fooderie.options;

import android.os.Bundle;

import com.example.fooderie.R;

import androidx.preference.PreferenceFragmentCompat;

public class OptionsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
