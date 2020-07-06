package de.isah.vocabtrainer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.commons.lang3.exception.ExceptionUtils;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyOnListException;
import de.isah.vocabtrainer.dictionary.exception.WordNotOnListException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

import java.util.regex.Pattern;

/**
 * Created by isa.heinze on 14.04.2018.
 */

public class EditWordActivity extends VocabTrainerAppCompatActivity {

    private Dictionary dictionary;
    private Word currentWord;
    private String separator;

    private Spinner prefixSpinner;
    private EditText swedishInput;
    private EditText germanInput;
    private EditText grammarInput;
    private EditText remarkInput;
    private CheckBox incompleteCheckbox;
    private Button buttonSave;
    private Button buttonAdd;
    private Button buttonDelete;
    private Button buttonDeleteWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SwedishVocabAppLogger.log("on create", EditWordActivity.class);

        this.separator = sharedPref.getString("pref_word_separator", ",");

        setContentView(R.layout.activity_edit_word);

        this.dictionary = DictionaryCache.getCachedDictionary();

        if(getIntent().getExtras() != null) {
            currentWord = this.dictionary.getAllWordsList().getWord(getIntent().getExtras().getString("wordkey"));
        } else {
            currentWord = new Word();
        }

        // since there is quite a lot of done with the buttons and text fields it makes sense to just save them in fields and reuse them.
        this.prefixSpinner = findViewById(R.id.spinnerWordPrefix);
        this.swedishInput = findViewById(R.id.editTextSwedish);
        this.germanInput = findViewById(R.id.editTextGerman);
        this.grammarInput = findViewById(R.id.editTextGrammar);
        this.remarkInput = findViewById(R.id.editTextRemarks);
        this.incompleteCheckbox = findViewById(R.id.checkBoxIncomplete);
        this.buttonSave = findViewById(R.id.buttonSaveWord);
        this.buttonAdd = findViewById(R.id.buttonAddToNewList);
        this.buttonDelete = findViewById(R.id.buttonRemoveFromNewList);
        this.buttonDeleteWord = findViewById(R.id.buttonDeleteFromDictionary);

        this.prefixSpinner.setSelection(mapPrefixToItem(currentWord.getPrefix()));
        this.swedishInput.setText(currentWord.getSwedish());
        this.germanInput.setText(currentWord.printGerman().replace(", ",this.separator));
        this.grammarInput.setText(currentWord.printGrammar().replace(", ",this.separator));
        this.remarkInput.setText(currentWord.printRemark());
        this.incompleteCheckbox.setChecked(currentWord.getState() instanceof WordStateIncomplete);

        setButtonVisibilities();

    }

    public void saveWord(View v){
        SwedishVocabAppLogger.log("saveWord", EditWordActivity.class);
        String swedish = this.swedishInput.getText().toString();
        String german = this.germanInput.getText().toString();

        if("".equals(swedish) || "".equals(german)){
            SwedishVocabAppLogger.log("Error: at least swedish and german must be specified", EditWordActivity.class);
            Snackbar.make(v, "At least swedish and german must be specified.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        // The word key consists of swdish and prefix. Since it is used in the Word Map, it cannot be changes just like that. A completely new word object is needed, the old one can be deleted.
        else if (!currentWord.getSwedish().equals(swedish) || !currentWord.getPrefix().string.equals(getWordPrefix(this.prefixSpinner).string)){
            boolean changed = false;
            try {
                WordBuilder builder = new WordBuilder();
                builder.addSwedish(swedish, getWordPrefix(this.prefixSpinner));
                changeWord(v, german, builder);

                Word newWord = builder.build();
                if(!incompleteCheckbox.isChecked()){
                    newWord.setState(new WordStateNew());
                } else {
                    newWord.setState(new WordStateIncomplete());
                }
                this.dictionary.addWord(newWord);

                this.dictionary.deleteWord(currentWord);

                SwedishVocabAppLogger.log("word was saved", EditWordActivity.class);
                changed = true;

            } catch (IllegalArgumentException | IllegalStateTransitionException e){
                SwedishVocabAppLogger.log("word could not be saved: "+ExceptionUtils.getStackTrace(e), EditWordActivity.class);
                changed = false;
            } catch (WordAlreadyExistsException e){
                SwedishVocabAppLogger.log("word could not be saved because a similar word already exists: "+ExceptionUtils.getStackTrace(e), AddWordActivity.class);
                changed = false;
            }  finally {
                String changedMessage;
                if(changed) {
                    changedMessage= "Word was changed.";
                } else {
                    changedMessage= "Word could not be changed.";
                }
                Snackbar.make(v, changedMessage, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        }
        else{
            boolean changed = false;
            try {
                WordBuilder builder = new WordBuilder(currentWord);
                changeWord(v, german, builder);

                SwedishVocabAppLogger.log("word was saved", EditWordActivity.class);
                changed = true;

            } catch (IllegalArgumentException e){
                SwedishVocabAppLogger.log("word could not be saved: "+ExceptionUtils.getStackTrace(e), EditWordActivity.class);
                changed = false;
            } finally {
                String changedMessage;
                if(changed) {
                    changedMessage= "Word was changed.";
                } else {
                    changedMessage= "Word could not be changed.";
                }
                Snackbar.make(v, changedMessage, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        }
    }

    private void changeWord(View v, String german, WordBuilder builder) {
        builder.addGerman(german.split(Pattern.quote(this.separator)));

        String grammar = this.grammarInput.getText().toString();
        builder.addGrammar(grammar.split(Pattern.quote(this.separator)));

        String remarks = this.remarkInput.getText().toString();
        builder.addRemark(remarks);

        if(currentWord.getState() instanceof WordStateIncomplete && !this.incompleteCheckbox.isChecked()){
            removeFromIncompleteAddToNew(v);
            setButtonVisibilities();
        }

        if(!(currentWord.getState() instanceof WordStateIncomplete) && this.incompleteCheckbox.isChecked()){
            removeFromAllListsAddToIncomplete(v);
            setButtonVisibilities();
        }
    }

    public void addToNewList(View v){
        SwedishVocabAppLogger.log("addToNewList", EditWordActivity.class);
        try{
            this.dictionary.addWordToNewList(currentWord);
            setButtonVisibilities();
        } catch (WordAlreadyOnListException e){
            SwedishVocabAppLogger.log("word was already on new list", EditWordActivity.class);
            Snackbar.make(v, "Word is already on new words list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } catch (IllegalStateTransitionException e){
            SwedishVocabAppLogger.log("word could not be added to new list: "+ ExceptionUtils.getStackTrace(e), EditWordActivity.class);
            Snackbar.make(v, "Word could not be added to new list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    private void removeFromIncompleteAddToNew(View v){
        SwedishVocabAppLogger.log("remove word from incomplete list", EditWordActivity.class);
        try{
            this.dictionary.removeWordFromIncompleteList(currentWord);
            setButtonVisibilities();
        } catch (WordNotOnListException e){
            SwedishVocabAppLogger.log("word was not on incomplete list. ", EditWordActivity.class);
            Snackbar.make(v, "Word is not on incomplete list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } catch (IllegalStateTransitionException e){
            SwedishVocabAppLogger.log("word could not be deleted from incomplete list. ", EditWordActivity.class);
            Snackbar.make(v, "Word could not be deleted from incomplete list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    private void removeFromAllListsAddToIncomplete(View v){
        SwedishVocabAppLogger.log("add to incomplete", EditWordActivity.class);
        try {
            this.dictionary.addWordToIncompleteList(currentWord);
            setButtonVisibilities();
        } catch (WordAlreadyExistsException e){
            SwedishVocabAppLogger.log("word already existed", EditWordActivity.class);
            Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } catch (WordAlreadyOnListException e){
            SwedishVocabAppLogger.log("word was already on incomplete list", EditWordActivity.class);
            Snackbar.make(v, "Word is already on incomplete list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } catch (IllegalStateTransitionException e) {
            SwedishVocabAppLogger.log("word could not be added to new list: "+ExceptionUtils.getStackTrace(e), EditWordActivity.class);
            Snackbar.make(v, "Word could not be added to word list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void removeFromNewList(View v){
        SwedishVocabAppLogger.log("remove from new list", EditWordActivity.class);
        try {
            this.dictionary.removeWordFromNewList(currentWord);
            setButtonVisibilities();
        } catch (WordNotOnListException e){
            SwedishVocabAppLogger.log("word was not on new list", EditWordActivity.class);
            Snackbar.make(v, "Word is not on new words list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } catch (IllegalStateTransitionException e){
            SwedishVocabAppLogger.log("word could not be removed from new list: "+ExceptionUtils.getStackTrace(e), EditWordActivity.class);
            Snackbar.make(v, "Word could not be removed from word list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void deleteWord(View v){
        SwedishVocabAppLogger.log("delete word", EditWordActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to delete the word permanently?");
        builder.setPositiveButton("Yes", new DoDeleteWordOnClickListener(v));
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }
        );
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setButtonVisibilities(){
        this.buttonAdd.setEnabled(currentWord.getState().enableAddToNewList());
        this.buttonDelete.setEnabled(currentWord.getState().enableDeleteFromNewList());
        this.buttonDeleteWord.setEnabled(!this.dictionary.isDisableDeleteWord());

        if(currentWord.getState().showAddToNewList()) {
            this.buttonAdd.setVisibility(View.VISIBLE);
        } else {
            this.buttonAdd.setVisibility(View.GONE);
        }
        if(currentWord.getState().showDeleteFromNewList()) {
            this.buttonDelete.setVisibility(View.VISIBLE);
        } else {
            this.buttonDelete.setVisibility(View.GONE);
        }
    }

    private void disableTextInput(){
        this.prefixSpinner.setEnabled(false);
        this.swedishInput.setEnabled(false);
        this.germanInput.setEnabled(false);
        this.grammarInput.setEnabled(false);
        this.remarkInput.setEnabled(false);
    }

    private void disableButtons(){
        this.buttonSave.setEnabled(false);
        this.buttonAdd.setEnabled(false);
        this.buttonDelete.setEnabled(false);
        this.buttonDeleteWord.setEnabled(false);
        this.incompleteCheckbox.setEnabled(false);
    }

    private boolean doDelete(){
        SwedishVocabAppLogger.log("do actual delete", EditWordActivity.class);
        boolean deleteSuccess = this.dictionary.deleteWord(currentWord);
        disableTextInput();
        disableButtons();
        SwedishVocabAppLogger.log("word was deleted? "+deleteSuccess, EditWordActivity.class);
        return deleteSuccess;
    }

    private int mapPrefixToItem(WordPrefix prefix){
        return AndroidSpinnerItemWordPrefixMapper.mapWordPrefixToSpinnerItem(prefix);
    }

    @NonNull
    private WordPrefix getWordPrefix(Spinner spinner) {
        return AndroidSpinnerItemWordPrefixMapper.mapSpinnerItemToWordPrefix((String) spinner.getSelectedItem());
    }

    private class DoDeleteWordOnClickListener implements DialogInterface.OnClickListener{

        private View v;

        DoDeleteWordOnClickListener(View v){
            this.v = v;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            boolean deleteSuccess = doDelete();
            dialog.dismiss();
            String deleteMessage;
            if(deleteSuccess){
                deleteMessage = "Word was deleted.";
            } else {
                deleteMessage = "Word could not be deleted.";
            }
            Snackbar.make(v, deleteMessage, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }
}
