package de.isah.vocabtrainer.dictionary.persist;

import de.isah.vocabtrainer.dictionary.LearnWordList;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.WordList;

import java.util.Queue;

/**
 * @author isa.heinze
 */

public interface Persistence {

    WordList getAllWords();
    Queue<Word> getNewWords();
    LearnWordList getToLearnWords();
    WordList getIncompleteList();

    boolean addWord(Word w);

    boolean persistAll(WordList list);

    boolean exportAll(WordList list);

    boolean importFile();

    boolean disableAddWords();

    boolean disableImportExport();

    boolean disableDeleteWords();

    boolean disableEditWords();
}
