package de.isah.vocabtrainer;

import android.preference.PreferenceFragment;
import android.os.Bundle;

/**
 * @author isa.heinze
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
