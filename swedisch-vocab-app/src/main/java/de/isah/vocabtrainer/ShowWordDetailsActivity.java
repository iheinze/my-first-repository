package de.isah.vocabtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;

/**
 * @author isa.heinze
 */
public class ShowWordDetailsActivity extends AppCompatActivity {

    private Dictionary dictionary;
    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word_details);

        b = getIntent().getExtras();

        // for display it is ok to create a new word from string, does not work for editing
        this.dictionary = DictionaryCache.getCachedDictionary();
        Word currentWord = this.dictionary.getAllWordsList().getWord(getIntent().getExtras().getString("wordkey"));

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
    }

    private String mapCompleteStatus(boolean incomplete) {
        if(!incomplete){
            return "yes";
        } else {
            return "no";
        }
    }

    public void editWord(View view){
        Intent intent = new Intent(this, EditWordActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}
