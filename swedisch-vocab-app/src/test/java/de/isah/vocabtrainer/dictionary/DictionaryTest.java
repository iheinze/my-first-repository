package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyOnListException;
import de.isah.vocabtrainer.dictionary.exception.WordNotOnListException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 09.01.2018.
 */

public class DictionaryTest {

    @Test
    public void testAddWord() throws IOException, WordAlreadyExistsException{
        Dictionary dictionary = new Dictionary("x");
        Integer nExpectedAll = dictionary.getNWordsInDict();
        Integer nExpectedNew = dictionary.getNWordsNew();

        //Note: in HardcodedValuesPersistence it is added twice, because we have the same list of words used at several places.
        Word newWord = new WordBuilder().addGerman("Test").addSwedish("Test", WordPrefix.NONE).build();
        dictionary.addWord(newWord);

        assertEquals(nExpectedAll+1,dictionary.getNWordsInDict());
        assertEquals(nExpectedNew+1,dictionary.getNWordsNew());
        assertEquals(Integer.valueOf(0), Integer.valueOf(dictionary.getToLearnList().size()));
        assertTrue(newWord.getState() instanceof WordStateNew);
        assertFalse(newWord.getState() instanceof WordStateLearn);
    }

    @Test
    public void testCreateToLearnList() throws IOException{
        Dictionary dictionary = new Dictionary("x");

        String expected = "Words added: 2/2";

        String result = dictionary.createToLearnList(2);

        assertEquals(Integer.valueOf(2), Integer.valueOf(dictionary.getToLearnList().size()));
        assertTrue(dictionary.getToLearnList().getWord(0).getState() instanceof WordStateLearn);
        assertTrue(dictionary.getToLearnList().getWord(0).getState() instanceof WordStateLearn);
        assertTrue(dictionary.getToLearnList().getWord(1).getState() instanceof WordStateLearn);

        assertEquals(expected, result);
    }

    @Test
    public void testCreateToLearnListAfterIncomplete() throws IOException, WordAlreadyExistsException, IllegalStateTransitionException, WordAlreadyOnListException {
        Dictionary dictionary = new Dictionary("x");

        // create inital learn list
        dictionary.createToLearnList(3);

        for(Word w : dictionary.getToLearnList().words){
            System.out.println(w.toString());
        }

        // simulating some learning
        Word w1 = dictionary.getToLearnListNoShuffle().words.get(0);
        System.out.println("word 1: " + w1.toString());
        Word w2 = dictionary.getToLearnListNoShuffle().words.get(1);
        System.out.println("word 2: " + w2.toString());
        Word w3 = dictionary.getToLearnListNoShuffle().words.get(2);
        System.out.println("word 3: " + w3.toString());

        w2.setState(new WordStateSGCorrect());
        w2.setState(new WordStateCorrect());

        // discover that one of the words is not correct (-1 from learn list)
        dictionary.addWordToIncompleteList(w1);

        // create a new list ( one additional word is removed because it was correct, should fill up to 2 again)
        dictionary.createToLearnList(3);

        assertEquals(Integer.valueOf(3), Integer.valueOf(dictionary.getToLearnList().size()));

    }

    @Test
    public void testCreateToLearnListNewWord() throws IOException, WordAlreadyExistsException {
        Dictionary dictionary = new Dictionary("x");
        Word wordToBeAdded = new WordBuilder().addGerman("Test").addSwedish("Test", WordPrefix.NONE).build();
        dictionary.addWord(wordToBeAdded);

        String expected = "Words added: 2/2";

        String result = dictionary.createToLearnList(2);

        assertEquals(Integer.valueOf(2), Integer.valueOf(dictionary.getToLearnList().size()));
        assertTrue(dictionary.getToLearnList().doesContain(wordToBeAdded));
        assertTrue(wordToBeAdded.getState() instanceof WordStateLearn);

        assertEquals(expected, result);
    }

    @Test
    public void testCreateToLearnListToFewWords() throws IOException{
        Dictionary dictionary = new Dictionary("x");

        dictionary.createToLearnList(10);
        assertEquals(Integer.valueOf(6), Integer.valueOf(dictionary.getToLearnList().size()));
    }

    @Test
    public void testCreateToLearnListToFewWordsTwo() throws IOException{
        Dictionary dictionary = new Dictionary("x");

        dictionary.createToLearnList(10);
        dictionary.createToLearnList(10);
        assertEquals(Integer.valueOf(6), Integer.valueOf(dictionary.getToLearnList().size()));
    }

    @Test
    public void testCreateToLearnListOneWordAlreadyOnToLearn() throws IOException, IllegalStateTransitionException, WordAlreadyExistsException {
        Dictionary dictionary = new Dictionary("x");
        dictionary.getAllWordsList().getWord(0).setState(new WordStateLearn());
        dictionary.getToLearnList().addWord(dictionary.getAllWordsList().getWord(0));
        dictionary.createToLearnList(10);
        assertEquals(Integer.valueOf(6), Integer.valueOf(dictionary.getToLearnList().size()));
    }

    @Test
    public void testDeleteWord() throws IOException, WordAlreadyExistsException {
        Dictionary dictionary = new Dictionary("x");
        int nExpectedAll = dictionary.getNWordsInDict();
        int nExpectedNew = dictionary.getNWordsNew();

        Word newWord = new WordBuilder().addGerman("Test").addSwedish("Test", WordPrefix.NONE).build();
        dictionary.addWord(newWord);
        dictionary.createToLearnList(1);
        dictionary.deleteWord(newWord);

        assertEquals(nExpectedAll,dictionary.getNWordsInDict());
        assertEquals(nExpectedNew,dictionary.getNWordsNew());
        assertEquals(Integer.valueOf(0), Integer.valueOf(dictionary.getToLearnList().size()));

    }

    @Test
    public void testRemoveWordFromNewList() throws IOException, WordAlreadyExistsException, IllegalStateTransitionException, WordNotOnListException {
        Dictionary dictionary = new Dictionary("x");
        int nExpectedAll = dictionary.getNWordsInDict();
        int nExpectedNew = dictionary.getNWordsNew();

        Word newWord = new WordBuilder().addGerman("Test").addSwedish("Test", WordPrefix.NONE).build();
        dictionary.addWord(newWord);
        dictionary.removeWordFromNewList(newWord);

        assertEquals(nExpectedAll+1,dictionary.getNWordsInDict());
        assertEquals(nExpectedNew,dictionary.getNWordsNew());

    }

    @Test
    public void testAddWordToNewList() throws IOException, WordAlreadyExistsException, IllegalStateTransitionException, WordNotOnListException, WordAlreadyOnListException {
        Dictionary dictionary = new Dictionary("x");
        int nExpectedAll = dictionary.getNWordsInDict();
        int nExpectedNew = dictionary.getNWordsNew();

        Word newWord = new WordBuilder().addGerman("Test").addSwedish("Test", WordPrefix.NONE).build();
        dictionary.addWord(newWord);
        dictionary.removeWordFromNewList(newWord);
        dictionary.addWordToNewList(newWord);

        assertEquals(nExpectedAll+1,dictionary.getNWordsInDict());
        assertEquals(nExpectedNew+1,dictionary.getNWordsNew());

    }

    @Test
    public void testGetPersistenceType() throws IOException{
        Dictionary dictionary = new Dictionary("x");

        assertEquals("HardcodedValuesPersistence", dictionary.getPersistenceType());
    }
}
