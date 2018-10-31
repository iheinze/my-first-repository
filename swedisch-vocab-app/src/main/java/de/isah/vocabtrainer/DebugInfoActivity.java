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

        String text = "---DEBUG---"
                + "\n" +"\n"
                + "App Name: "
                + getResources().getString(R.string.app_name)
                + "\n"
                + "App Version: "
                + getResources().getString(R.string.app_version)
                + "\n" +"\n"
                + "Persistence type: "
                + dictionary.getPersistenceType()
                + "\n" +"\n"
                + "Number of words in dictionary: "
                + dictionary.getNWordsInDict()
                + "\n"
                +"Number of new words: "
                + dictionary.getNWordsNew()
                +"\n"
                + "Number of words in toLearn List: "
                + dictionary.getToLearnList().size()
                + "\n"
                + "Number of words in incomplete List: "
                + dictionary.getIncompleteList().size()
                + "\n" + "\n"
                + "File Path: "
                + FileConstants.getFilePath()
                + "\n"
                + "External File Path: "
                + FileConstants.getExternalFilePath()
                + "\n";

        TextView textView = findViewById(R.id.textViewDebugInfo);
        textView.setText(text);
    }

}
