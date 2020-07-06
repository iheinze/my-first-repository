package de.isah.vocabtrainer;

import android.os.Bundle;
import android.widget.TextView;

import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;


/**
 *
 */
public class InfoActivity extends VocabTrainerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwedishVocabAppLogger.log("on create", InfoActivity.class);
        setContentView(R.layout.activity_info);

        TextView textView1 = findViewById(R.id.textViewAppInfoName);
        textView1.setText(R.string.app_name);

        TextView textView2 = findViewById(R.id.textViewAppInfoVersion);
        textView2.setText(R.string.app_version);
    }

}
