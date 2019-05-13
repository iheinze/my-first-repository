package de.isah.vocabtrainer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

/**
 *
 */
public class DebugInfoActivity extends VocabTrainerAppCompatActivity {

    private DebugInfoHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwedishVocabAppLogger.log("on create", DebugInfoActivity.class, isDebug);

        setContentView(R.layout.activity_debug_info);

        Dictionary dictionary = DictionaryCache.getCachedDictionary();
        this.handler = new DebugInfoHandler();

        String text = getDebugInfo(dictionary);

        TextView textView = findViewById(R.id.textViewDebugInfo);
        textView.setText(text);
    }

    @NonNull
    private String getDebugInfo(Dictionary dictionary) {
        return this.handler.getDebugInfo(dictionary, getResources().getString(R.string.app_name), getResources().getString(R.string.app_version));
    }

}
