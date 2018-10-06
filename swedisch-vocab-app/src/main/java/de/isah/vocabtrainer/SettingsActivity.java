package de.isah.vocabtrainer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author isa.heinze
 */
public class SettingsActivity extends VocabTrainerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();


    }


}
