package de.isah.vocabtrainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by isa.heinze on 23.06.2018.
 */

public abstract class VocabTrainerAppCompatActivity extends AppCompatActivity {

    protected SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }

    //mainly copied from super class
    @Override
    public boolean onSupportNavigateUp() {
        Intent upIntent = getSupportParentActivityIntent();

        if (upIntent != null) {
            upIntent.putExtra(MainActivity.PREVIOUS_FRAGMENT, this.getIntent().getIntExtra(MainActivity.PREVIOUS_FRAGMENT,0));

            if (supportShouldUpRecreateTask(upIntent)) {
                TaskStackBuilder b = TaskStackBuilder.create(this);
                onCreateSupportNavigateUpTaskStack(b);
                onPrepareSupportNavigateUpTaskStack(b);
                b.startActivities();

                try {
                    ActivityCompat.finishAffinity(this);
                } catch (IllegalStateException e) {
                    finish();
                }
            } else {
                supportNavigateUpTo(upIntent);
            }
            return true;
        }
        return false;
    }
}
