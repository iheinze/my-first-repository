package de.isah.vocabtrainer;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

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

public class AddWordHandler {

    @NonNull
    String addWord(Dictionary dictionary, String swedish, String german, WordPrefix prefix, String grammar, String remarks, boolean isIncomplete, String separator) throws IllegalStateTransitionException, WordAlreadyExistsException {
        WordBuilder builder = new WordBuilder();
        builder.addSwedish(swedish, prefix).addGerman(german.split(Pattern.quote(separator)));
        if (!("".equals(grammar)) && !(" ".equals(grammar))) {
            builder.addGrammar(grammar.split(Pattern.quote(separator)));
        }
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
        String addedMessage = "Word was added.";
        SwedishVocabAppLogger.log("word was added", AddWordActivity.class);
        return addedMessage;
    }
}
