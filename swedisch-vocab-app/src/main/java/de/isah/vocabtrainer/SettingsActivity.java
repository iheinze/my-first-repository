package de.isah.vocabtrainer;

import android.os.Bundle;

import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

/**
 * @author isa.heinze
 */
public class SettingsActivity extends VocabTrainerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwedishVocabAppLogger.log("on create", SettingsActivity.class);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();


    }


}
