package de.isah.vocabtrainer.dictionary.persist;

import de.isah.vocabtrainer.dictionary.LearnWordList;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.persist.filehandling.AbstractFileHandler;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.WordList;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * A persistence type which takes words from a file which is delivered along with the app.
 * <p>
 * Created by isa.heinze on 02.02.2018.
 */

public class AppFilePersistence implements Persistence {

    private WordList allWords;
    private LearnWordList toLearnWords;
    private Queue<Word> newWords;
    private WordList incompleteList;


    AppFilePersistence() {
        this.allWords = new WordList();
        this.toLearnWords = new LearnWordList();
        this.newWords = new LinkedList<>();
        this.incompleteList = new WordList();
        getWords();
    }

    private void getWords() {
        System.out.println("getting words");
        if (AbstractFileHandler.getFileInStream() != null) {
            Scanner scanner = new Scanner(AbstractFileHandler.getFileInStream(), "UTF-8").useDelimiter("--");
            while (scanner.hasNext()) {
                try {
                    String string = scanner.next().trim();

                    // check for comment line
                    if (string.startsWith("#")) {
                        continue;
                    }
                    System.out.println(string);
                    Word word = new Word(string);
                    this.allWords.addWord(word);

                    if (word.getState() instanceof WordStateNew) {
                        this.newWords.add(word);
                    }
                    if (word.getState() instanceof WordStateIncomplete) {
                        this.incompleteList.addWord(word);
                    }
                    if (word.getState() instanceof WordStateLearn) {
                        this.toLearnWords.addWord(word);
                    }

                    // Note: learning progress (correct states) cannot be saved in app file, so it is not needed to be taken into account

                } catch (WordAlreadyExistsException e){
                    // do nothing
                }

            }
        }
    }

    @Override
    public WordList getAllWords() {
        return this.allWords;
    }

    @Override
    public Queue<Word> getNewWords() {
        return this.newWords;
    }

    @Override
    public LearnWordList getToLearnWords() {
        return this.toLearnWords;
    }

    @Override
    public WordList getIncompleteList() {
        return this.incompleteList;
    }

    @Override
    public boolean addWord(Word w){
        return false;
    }

    @Override
    public boolean persistAll(WordList list) {
        return false;
    }

    @Override
    public boolean exportAll(WordList list) {
        return false;
    }

    @Override
    public boolean importFile() {
        return false;
    }

    @Override
    public boolean disableAddWords() {
        return true;
    }

    @Override
    public boolean disableImportExport() {
        return true;
    }

    @Override
    public boolean disableDeleteWords() {
        return true;
    }

    @Override
    public boolean disableEditWords() {
        return true;
    }
}
