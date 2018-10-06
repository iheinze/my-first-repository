package de.isah.vocabtrainer;

import android.os.Bundle;
import android.widget.TextView;


/**
 *
 */
public class InfoActivity extends VocabTrainerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        StringBuilder statsStringBuilder = new StringBuilder();
        statsStringBuilder.append("App Name: ");
        statsStringBuilder.append(getResources().getString(R.string.app_name));
        statsStringBuilder.append("\n");
        statsStringBuilder.append("App Version: ");
        statsStringBuilder.append(getResources().getString(R.string.app_version));
        statsStringBuilder.append("\n");

        TextView textView1 = (TextView) findViewById(R.id.textViewAppInfoName);
        textView1.setText(R.string.app_name);

        TextView textView2 = (TextView) findViewById(R.id.textViewAppInfoVersion);
        textView2.setText(R.string.app_version);
    }

}
