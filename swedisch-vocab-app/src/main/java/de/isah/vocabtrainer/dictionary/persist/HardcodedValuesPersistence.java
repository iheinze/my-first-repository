package de.isah.vocabtrainer.dictionary.persist;

import de.isah.vocabtrainer.dictionary.LearnWordList;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.WordList;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateDictionary;

import java.util.LinkedList;
import java.util.Queue;

/**
 * For JUnit tests,
 * TODO can be moved to tests since it is not needed in real app
 *
 * @author isa.heinze
 */

public class HardcodedValuesPersistence implements Persistence {

    private WordList hardcodedWords;

    HardcodedValuesPersistence() {
        try {
            this.hardcodedWords = new WordList();
            this.hardcodedWords.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
            this.hardcodedWords.addWord(new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.EN).addGrammar("pojken", "pojkar", "pojkarna").build());
            this.hardcodedWords.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.EN).addGrammar("pennan", "pennor", "pennorna").build());
            this.hardcodedWords.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).addGrammar("bra", "bra").build());
            this.hardcodedWords.addWord(new WordBuilder().addGerman("drei").addSwedish("tre", WordPrefix.NONE).build());
            this.hardcodedWords.addWord(new WordBuilder().addGerman("vier").addSwedish("fyra", WordPrefix.NONE).build());

            for (int i = 0; i < this.hardcodedWords.size(); i++) {
                this.hardcodedWords.getWord(i).setState(new WordStateDictionary());
            }
        } catch (IllegalStateTransitionException e){
            //do nothing
        } catch (WordAlreadyExistsException e) {
            // do nothing
        }
    }

    @Override
    public WordList getAllWords() {
        return this.hardcodedWords;
    }

    @Override
    public Queue<Word> getNewWords() {
        return new LinkedList<>();
    }

    @Override
    public LearnWordList getToLearnWords() {
        return new LearnWordList();
    }

    @Override
    public WordList getIncompleteList() {
        return new WordList();
    }

    @Override
    public boolean addWord(Word w) {
        return true;
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
        return false;
    }

    @Override
    public boolean disableImportExport() {
        return true;
    }

    @Override
    public boolean disableDeleteWords() {
        return false;
    }
}
