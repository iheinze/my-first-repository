package de.isah.vocabtrainer;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.commons.lang3.exception.ExceptionUtils;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

import java.util.regex.Pattern;

/**
 * @author isa.heinze
 */
public class AddWordActivity extends VocabTrainerAppCompatActivity {

    private AddWordHandler handler;
    private Dictionary dictionary;
    private String separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwedishVocabAppLogger.log("on create", AddWordActivity.class, isDebug);

        setContentView(R.layout.activity_add_word);

        initialize();
    }

    void initialize(){
        this.handler = new AddWordHandler();
        this.separator = sharedPref.getString("pref_word_separator", ",");
        this.dictionary = DictionaryCache.getCachedDictionary();
    }

    public void addWord(View view){

        SwedishVocabAppLogger.log("add word", AddWordActivity.class, isDebug);

        String swedish = ((EditText) findViewById(R.id.editTextSwedish)).getText().toString();
        String german = ((EditText) findViewById(R.id.editTextGerman)).getText().toString();

        if("".equals(swedish) || "".equals(german)){
            SwedishVocabAppLogger.log("Error: at least swedish and german must be specified", AddWordActivity.class, isDebug);
            Snackbar.make(view, "At least swedish and german must be specified.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        else{
            Spinner spinner = findViewById(R.id.spinnerWordPrefix);
            CheckBox checkbox = findViewById(R.id.checkBoxIncomplete);
            boolean isIncomplete = checkbox.isChecked();

            String addedMessage = "Word could not be added.";
            try {
                addedMessage = addWord(swedish, german, getWordPrefix(spinner), isIncomplete);
            } catch (IllegalArgumentException | IllegalStateTransitionException e){
                SwedishVocabAppLogger.log("word could not be added: "+ExceptionUtils.getStackTrace(e), AddWordActivity.class, isDebug);
                addedMessage = "Word could not be added.";
            } catch (WordAlreadyExistsException e){
                SwedishVocabAppLogger.log("word could not be added: "+ExceptionUtils.getStackTrace(e), AddWordActivity.class, isDebug);
                addedMessage = e.getMessage();
            } finally {
                Snackbar.make(view, addedMessage, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        }

        ((EditText) findViewById(R.id.editTextSwedish)).setText("");
        ((EditText) findViewById(R.id.editTextGerman)).setText("");
        ((EditText) findViewById(R.id.editTextGrammar)).setText("");
        ((EditText) findViewById(R.id.editTextRemarks)).setText("");
        ((Spinner) findViewById(R.id.spinnerWordPrefix)).setSelection(0);
        ((CheckBox) findViewById(R.id.checkBoxIncomplete)).setChecked(false);

    }

    @NonNull
    String addWord(String swedish, String german, WordPrefix prefix, boolean isIncomplete) throws IllegalStateTransitionException, WordAlreadyExistsException {
        String grammar = ((EditText) findViewById(R.id.editTextGrammar)).getText().toString();
        String remarks = ((EditText) findViewById(R.id.editTextRemarks)).getText().toString();
        return this.handler.addWord(swedish, german, prefix, grammar, remarks, isIncomplete, this.separator, this.isDebug);
    }

    @NonNull
    private WordPrefix getWordPrefix(Spinner spinner) {
        return AndroidSpinnerItemWordPrefixMapper.mapSpinnerItemToWordPrefix((String) spinner.getSelectedItem());
    }
}
