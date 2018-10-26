package de.isah.vocabtrainer;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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

    private Dictionary dictionary;
    private String separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwedishVocabAppLogger.log("on create", AddWordActivity.class, isDebug);

        this.separator = sharedPref.getString("pref_word_separator", ",");

        setContentView(R.layout.activity_add_word);

        dictionary = DictionaryCache.getCachedDictionary();
    }

    public void addWord(View view){

        SwedishVocabAppLogger.log("add word", AddWordActivity.class, isDebug);

        String swedish = ((EditText) findViewById(R.id.editTextSwedish)).getText().toString();
        String german = ((EditText) findViewById(R.id.editTextGerman)).getText().toString();

        if("".equals(swedish) || "".equals(german)){
            SwedishVocabAppLogger.log("Error: at least swdish and german must be specified", AddWordActivity.class, isDebug);
            Snackbar.make(view, "At least swedish and german must be specified.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        else{
            Spinner spinner = findViewById(R.id.spinnerWordPrefix);
            CheckBox checkbox = findViewById(R.id.checkBoxIncomplete);
            boolean isIncomplete = checkbox.isChecked();

            String addedMessage = "Word could not be added.";
            try {

                WordBuilder builder = new WordBuilder();
                builder.addSwedish(swedish, getWordPrefix(spinner)).addGerman(german.split(Pattern.quote(this.separator)));
                String grammar = ((EditText) findViewById(R.id.editTextGrammar)).getText().toString();
                if (!("".equals(grammar)) && !(" ".equals(grammar))) {
                    builder.addGrammar(grammar.split(Pattern.quote(this.separator)));
                }
                String remarks = ((EditText) findViewById(R.id.editTextRemarks)).getText().toString();
                if (!("".equals(remarks)) && !(" ".equals(remarks))) {
                    builder.addRemark(remarks);
                }

                Word addedWord = builder.build();
                if(!isIncomplete){
                    addedWord.setState(new WordStateNew());
                } else {
                    addedWord.setState(new WordStateIncomplete());
                }
                dictionary.addWord(addedWord);
                addedMessage = "Word was added.";
                SwedishVocabAppLogger.log("word was added", AddWordActivity.class, isDebug);
                dictionary = DictionaryCache.getCachedDictionary();
            } catch (IllegalArgumentException e){
                SwedishVocabAppLogger.log("word could not be added: "+e.getStackTrace(), AddWordActivity.class, isDebug);
                addedMessage = "Word could not be added.";
            } catch (IllegalStateTransitionException e){
                SwedishVocabAppLogger.log("word could not be added: "+e.getStackTrace(), AddWordActivity.class, isDebug);
                addedMessage = "Word could not be added.";
            } catch (WordAlreadyExistsException e){
                SwedishVocabAppLogger.log("word could not be added: "+e.getStackTrace(), AddWordActivity.class, isDebug);
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
    private WordPrefix getWordPrefix(Spinner spinner) {
        return AndroidSpinnerItemWordPrefixMapper.mapSpinnerItemToWordPrefix((String) spinner.getSelectedItem());
    }
}
