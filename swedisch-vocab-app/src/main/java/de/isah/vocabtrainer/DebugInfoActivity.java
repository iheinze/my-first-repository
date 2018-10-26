package de.isah.vocabtrainer;

import android.os.Bundle;
import android.widget.TextView;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

/**
 *
 */
public class DebugInfoActivity extends VocabTrainerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwedishVocabAppLogger.log("on create", DebugInfoActivity.class, isDebug);

        setContentView(R.layout.activity_debug_info);

        Dictionary dictionary = DictionaryCache.getCachedDictionary();

        StringBuilder statsStringBuilder = new StringBuilder();
        statsStringBuilder.append("---DEBUG---");
        statsStringBuilder.append("\n");
        statsStringBuilder.append("\n");
        statsStringBuilder.append("App Name: ");
        statsStringBuilder.append(getResources().getString(R.string.app_name));
        statsStringBuilder.append("\n");
        statsStringBuilder.append("App Version: ");
        statsStringBuilder.append(getResources().getString(R.string.app_version));
        statsStringBuilder.append("\n");
        statsStringBuilder.append("\n");
        statsStringBuilder.append("Persistence type: ");
        statsStringBuilder.append(dictionary.getPersistenceType());
        statsStringBuilder.append("\n");
        statsStringBuilder.append("\n");
        statsStringBuilder.append("Number of words in dictionary: ");
        statsStringBuilder.append(dictionary.getNWordsInDict());
        statsStringBuilder.append("\n");
        statsStringBuilder.append("Number of new words: ");
        statsStringBuilder.append(dictionary.getNWordsNew());
        statsStringBuilder.append("\n");
        statsStringBuilder.append("Number of words in toLearn List: ");
        statsStringBuilder.append(dictionary.getToLearnList().size());
        statsStringBuilder.append("\n");
        statsStringBuilder.append("Number of words in incomplete List: ");
        statsStringBuilder.append(dictionary.getIncompleteList().size());
        statsStringBuilder.append("\n");
        statsStringBuilder.append("\n");
        statsStringBuilder.append("File Path: ");
        statsStringBuilder.append(FileConstants.getFilePath());
        statsStringBuilder.append("\n");
        statsStringBuilder.append("External File Path: ");
        statsStringBuilder.append(FileConstants.getExternalFilePath());
        statsStringBuilder.append("\n");

        TextView textView = findViewById(R.id.textViewDebugInfo);
        textView.setText(statsStringBuilder.toString());
    }

}
