package de.isah.vocabtrainer;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;

import java.util.regex.Pattern;

/**
 * Created by isa.heinze on 14.04.2018.
 */

public class EditWordActivity extends AppCompatActivity {

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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        this.separator = sharedPref.getString("pref_word_separator", ",");

        setContentView(R.layout.activity_edit_word);

        this.dictionary = DictionaryCache.getCachedDictionary();

        //TODO: check for null
        currentWord = this.dictionary.getAllWordsList().getWord(getIntent().getExtras().getString("wordkey"));

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
        // TODO check for null
        String swedish = this.swedishInput.getText().toString();
        String german = this.germanInput.getText().toString();

        if(swedish == null || german == null || "".equals(swedish) || "".equals(german)){
            Snackbar.make(v, "At least swedish and german must be specified.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        else{
            boolean changed = false;
            try {
                WordBuilder builder = new WordBuilder(currentWord);
                builder.addSwedish(swedish, getWordPrefix(this.prefixSpinner)).addGerman(german.split(Pattern.quote(this.separator)));

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

                changed = true;

            } catch (IllegalArgumentException e){
                changed = false;
            } finally {
                String changedMessage;
                if(changed) {
                    changedMessage= "Word was changed.";
                } else {
                    changedMessage= "Word could not be changed changed.";
                }
                Snackbar.make(v, changedMessage, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        }
    }

    public void addToNewList(View v){
        if(!(currentWord.getState() instanceof WordStateNew)){
            this.dictionary.addWordToNewList(currentWord);
            setButtonVisibilities();
        } else {
            Snackbar.make(v, "Word is already on new words list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    private void removeFromIncompleteAddToNew(View v){
        if(currentWord.getState() instanceof WordStateIncomplete){
            this.dictionary.removeWordFromIncompleteList(currentWord);
            setButtonVisibilities();
        } else {
            Snackbar.make(v, "Word is not on incomplete list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    private void removeFromAllListsAddToIncomplete(View v){
        try {
            if (!(currentWord.getState() instanceof WordStateIncomplete)) {
                this.dictionary.addWordToIncompleteList(currentWord);
                setButtonVisibilities();

            } else {
                Snackbar.make(v, "Word is already on incomplete list.", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        } catch (WordAlreadyExistsException e){
            Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void removeFromNewList(View v){
        if(currentWord.getState() instanceof WordStateNew){
            this.dictionary.removeWordFromNewList(currentWord);
            setButtonVisibilities();
        } else {
            Snackbar.make(v, "Word is not on new words list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void deleteWord(View v){
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
        boolean deleteSuccess = this.dictionary.deleteWord(currentWord);
        disableTextInput();
        disableButtons();
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

        public DoDeleteWordOnClickListener(View v){
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
