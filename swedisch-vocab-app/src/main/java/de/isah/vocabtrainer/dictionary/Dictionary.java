package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyOnListException;
import de.isah.vocabtrainer.dictionary.exception.WordNotOnListException;
import de.isah.vocabtrainer.dictionary.persist.Persistence;
import de.isah.vocabtrainer.dictionary.persist.PersistenceFactory;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateDictionary;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

/**
 * Class handles everything related to dictionary and word lists.
 *
 * @author isa.heinze
 */

public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    private WordList dictionary;
    private LearnWordList toLearn;
    private Queue<Word> newWords;
    private WordList incompleteList;

    private Persistence persistence;

    public Dictionary(String persistenceType) throws IOException {
        this.persistence = new PersistenceFactory().create(persistenceType);
        this.dictionary = this.persistence.getAllWords();
        this.newWords = this.persistence.getNewWords();
        this.toLearn = this.persistence.getToLearnWords();
        this.incompleteList = this.persistence.getIncompleteList();
    }

    // TODO: to learn specific
    public String createToLearnList(int toLearnListLength) {
        this.toLearn.clearListFromKnownWords();
        int afterClear = this.toLearn.size();

        StringBuilder builder = new StringBuilder();

        // if all words are on the toLearn list, do not add any words
        int countNew = 0;
        int countOld = 0;
        if (afterClear < this.dictionary.size()) {

            //fill toLearnList with with words from the newWordsList
            while (this.toLearn.size() < toLearnListLength && newWords.size() > 0) {
                try {
                    Word newWord = newWords.poll();
                    if(newWord != null) {
                        newWord.setState(new WordStateLearn());
                        this.toLearn.addWord(newWord);
                        countNew++;
                    }
                } catch (IllegalStateTransitionException | WordAlreadyExistsException  e){
                    // do nothing
                }
            }

            // fill remaining places with random words
            ArrayList<Integer> wordIds = getRandomWordIds();
            // get the maximum number of words that can be added randomly.
            int nMissingWords = toLearnListLength - this.toLearn.size();
            int maxWordsToAdd = wordIds.size() >= nMissingWords ? nMissingWords : wordIds.size();
            for (int i = 0; i < maxWordsToAdd; i++) {
                try {
                    Word randWord = this.dictionary.getWord(wordIds.get(i));
                    randWord.setState(new WordStateLearn());
                    this.toLearn.addWord(randWord);
                    countOld++;
                } catch (IllegalStateTransitionException | WordAlreadyExistsException e){
                    // do nothing
                }
            }

            builder.append("Words added: ").append(countNew + countOld).append("/").append(this.toLearn.size());
        } else {
            builder.append("No words added: All words are already on learn list.");
        }

        // reset learning progress
        resetProgress();

        return builder.toString();
    }

    // TODO: this is toLearn specific
    public void resetProgress(){
        for (int i = 0; i < this.toLearn.size(); i++) {
            try {
                Word w = this.toLearn.getWord(i);
                w.setState(new WordStateLearn());
            } catch (IllegalStateTransitionException e){
                // do nothing just continue
            }
        }
    }

    // TODO: this is toLearn specific
    private ArrayList<Integer> getRandomWordIds() {
        ArrayList<Integer> wordIds = new ArrayList<>();
        for (int i = 0; i < this.dictionary.size(); i++) {
            if (this.dictionary.getWord(i).getState() instanceof WordStateDictionary) {
                wordIds.add(i);
            }
        }
        Collections.shuffle(wordIds);

        return wordIds;
    }

    public LearnWordList getToLearnList() {
        this.toLearn.shuffle();
        return this.toLearn;
    }

    public Word getRandomWord(){
        if(this.dictionary.size() > 0) {
            this.dictionary.shuffle();
            return this.dictionary.getWord(0);
        } else {
            return null;
        }
    }

    LearnWordList getToLearnListNoShuffle() {
        return this.toLearn;
    }

    public WordList getIncompleteList() {
        return this.incompleteList;
    }

    public WordList getAllWordsList() {
        return this.dictionary;
    }

    public boolean addWord(Word word) throws WordAlreadyExistsException{
        try {
            if (!(word.getState() instanceof WordStateIncomplete)) {
                word.setState(new WordStateNew());
            } else {
                word.setState(new WordStateIncomplete());
            }
            boolean added = this.persistence.addWord(word);
            if (!added) {
                return false;
            }

            this.dictionary.addWord(word);

            if (!(word.getState() instanceof WordStateIncomplete)) {
                this.newWords.add(word);
            } else {
                this.incompleteList.addWord(word);
            }

            return true;
        } catch (IllegalStateTransitionException e) {
            return false;
        }

    }

    public void addWordToNewList(Word word) throws IllegalStateTransitionException, WordAlreadyOnListException {
        if(!(word.getState() instanceof WordStateNew)) {
            word.setState(new WordStateNew());
            this.newWords.add(word);
        } else {
            throw new WordAlreadyOnListException("Word "+word.getKey()+" was already on new list.");
        }
    }

    public void removeWordFromNewList(Word word) throws IllegalStateTransitionException, WordNotOnListException {
        if(word.getState() instanceof WordStateNew){
            word.setState(new WordStateDictionary());
            this.newWords.remove(word);
        } else {
            throw new WordNotOnListException("the word was "+word.getKey()+" was not on the new list");
        }
    }

    public boolean deleteWord(Word word) {
        this.toLearn.removeWord(word);
        this.newWords.remove(word);
        this.dictionary.removeWord(word);

        return true;
    }

    public int getNWordsInDict() {
        return this.dictionary.size();
    }

    public int getNWordsNew() {
        return this.newWords.size();
    }

    public boolean export() {
        boolean persist = this.persistence.persistAll(this.dictionary);
        boolean export = this.persistence.exportAll(this.dictionary);
        return persist && export;
    }

    public boolean persist() {
        return this.persistence.persistAll(this.dictionary);
    }

    public boolean importDir() {
        boolean importResult = this.persistence.importFile();
        this.dictionary = this.persistence.getAllWords();
        this.newWords = this.persistence.getNewWords();
        this.toLearn = this.persistence.getToLearnWords();
        return importResult;
    }

    public void removeWordFromIncompleteList(Word word) throws IllegalStateTransitionException, WordNotOnListException {
        if(word.getState() instanceof WordStateIncomplete) {
            word.setState(new WordStateNew());
            this.incompleteList.removeWord(word);
            this.newWords.add(word);
        } else {
            throw new WordNotOnListException("word was not on the incomplete list");
        }
    }

    public void addWordToIncompleteList(Word word) throws WordAlreadyExistsException, IllegalStateTransitionException, WordAlreadyOnListException{
        if (!(word.getState() instanceof WordStateIncomplete)) {
            word.setState(new WordStateIncomplete());
            this.incompleteList.addWord(word);
            this.toLearn.removeWord(word);
            this.newWords.remove(word);
        } else {
            throw new WordAlreadyOnListException("word was already on incomplete list");
        }
    }

    public boolean isDisableAddWord() {
        return this.persistence.disableAddWords();
    }

    public boolean isDisableImportExport() {
        return this.persistence.disableImportExport();
    }

    public boolean isDisableDeleteWord() {
        return this.persistence.disableDeleteWords();
    }

    public boolean isDisableEditWord() {
        return this.persistence.disableEditWords();
    }

    public String getPersistenceType(){
        return this.persistence.getClass().getSimpleName();
    }
}
