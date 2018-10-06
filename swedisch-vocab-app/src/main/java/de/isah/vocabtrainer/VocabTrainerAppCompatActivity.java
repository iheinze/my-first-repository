package de.isah.vocabtrainer;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by isa.heinze on 23.06.2018.
 */

public class VocabTrainerAppCompatActivity extends AppCompatActivity {

    //mainly copied from super class
    @Override
    public boolean onSupportNavigateUp() {
        Intent upIntent = getSupportParentActivityIntent();
        upIntent.putExtra(MainActivity.PREVIOUS_FRAGMENT, this.getIntent().getIntExtra(MainActivity.PREVIOUS_FRAGMENT,0));

        if (upIntent != null) {
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
