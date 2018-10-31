package de.isah.vocabtrainer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.exception.ExceptionUtils;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyOnListException;
import de.isah.vocabtrainer.dictionary.exception.WordNotOnListException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

/**
 * @author isa.heinze
 */
public class ShowWordDetailsActivity extends VocabTrainerAppCompatActivity {

    private Dictionary dictionary;
    private Bundle b;

    private Button buttonAdd;
    private Button buttonDelete;
    private Button buttonDeleteWord;
    private Button buttonEdit;
    private Word currentWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwedishVocabAppLogger.log("on create", ShowWordDetailsActivity.class, isDebug);
        setContentView(R.layout.activity_show_word_details);

        b = getIntent().getExtras();

        // for display it is ok to create a new word from string, does not work for editing
        this.dictionary = DictionaryCache.getCachedDictionary();
        if(getIntent().getExtras() != null) {
            currentWord = this.dictionary.getAllWordsList().getWord(getIntent().getExtras().getString("wordkey"));
        } else {
            currentWord = new Word();
        }

        TextView textViewSwedish = findViewById(R.id.textViewShowWordDetailsSwedish);
        textViewSwedish.setText(currentWord.printSwedishAndGrammar());

        TextView textViewGerman = findViewById(R.id.textViewShowWordDetailsGerman);
        textViewGerman.setText(currentWord.printGerman());

        TextView textViewRemark = findViewById(R.id.textViewShowWordDetailsRemark);
        textViewRemark.setText(currentWord.printRemark());

        TextView textViewComplete = findViewById(R.id.textViewShowWordDetailsComplete);
        textViewComplete.setText(mapCompleteStatus(currentWord.getState() instanceof WordStateIncomplete));

        TextView textViewLists = findViewById(R.id.textViewShowWordDetailsLists);
        textViewLists.setText(currentWord.printLists());

        this.buttonAdd = findViewById(R.id.buttonAddToNewList);
        this.buttonDelete = findViewById(R.id.buttonRemoveFromNewList);
        this.buttonDeleteWord = findViewById(R.id.buttonDeleteFromDictionary);
        this.buttonEdit = findViewById(R.id.buttonEditWord);

        setButtonVisibilities();
    }

    private String mapCompleteStatus(boolean incomplete) {
        if(!incomplete){
            return "yes";
        } else {
            return "no";
        }
    }

    public void addToNewList(View v){
        SwedishVocabAppLogger.log("add to new list", ShowWordDetailsActivity.class, isDebug);
        try{
            this.dictionary.addWordToNewList(currentWord);
            setButtonVisibilities();
        } catch (WordAlreadyOnListException e){
            SwedishVocabAppLogger.log("word was already on new list", ShowWordDetailsActivity.class, isDebug);
            Snackbar.make(v, "Word is already on new words list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } catch (IllegalStateTransitionException e){
            SwedishVocabAppLogger.log("word could not be added to new list: "+ExceptionUtils.getStackTrace(e), ShowWordDetailsActivity.class, isDebug);
            Snackbar.make(v, "Word could not be added to new list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void removeFromNewList(View v){
        SwedishVocabAppLogger.log("remove from new list", ShowWordDetailsActivity.class, isDebug);
        try{
            this.dictionary.removeWordFromNewList(currentWord);
            setButtonVisibilities();
        } catch (WordNotOnListException e){
            SwedishVocabAppLogger.log("word was not on new list", ShowWordDetailsActivity.class, isDebug);
            Snackbar.make(v, "Word is not on new words list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } catch ( IllegalStateTransitionException e){
            SwedishVocabAppLogger.log("word could not be removed from new list: "+ExceptionUtils.getStackTrace(e), ShowWordDetailsActivity.class, isDebug);
            Snackbar.make(v, "Word could not be removed from new list.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void deleteWord(View v){
        SwedishVocabAppLogger.log("delete word", ShowWordDetailsActivity.class, isDebug);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to delete the word permanently?");
        builder.setPositiveButton("Yes", new ShowWordDetailsActivity.DoDeleteWordOnClickListener(v));
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
        this.buttonEdit.setEnabled(!this.dictionary.isDisableEditWord());

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

    public void editWord(View view){
        SwedishVocabAppLogger.log("edit word", ShowWordDetailsActivity.class, isDebug);
        Intent intent = new Intent(this, EditWordActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    private boolean doDelete(){
        SwedishVocabAppLogger.log("do actual delete", ShowWordDetailsActivity.class, isDebug);
        boolean deleteSuccess = this.dictionary.deleteWord(currentWord);
        disableButtons();
        return deleteSuccess;
    }

    private void disableButtons(){
        this.buttonAdd.setEnabled(false);
        this.buttonDelete.setEnabled(false);
        this.buttonDeleteWord.setEnabled(false);
        this.buttonEdit.setEnabled(false);
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
