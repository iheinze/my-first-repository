package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateGSCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 15.06.2018.
 */
public class LearnWordListTest {

    @Test
    public void testClearListFromKnownWords() throws IllegalStateTransitionException, WordAlreadyExistsException {
        LearnWordList words = new LearnWordList();

        Word word1 = new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build();
        word1.setState(new WordStateNew());
        word1.setState(new WordStateLearn());
        word1.setState(new WordStateSGCorrect());
        word1.setState(new WordStateCorrect());
        Word word2 = new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build();
        word2.setState(new WordStateNew());
        word2.setState(new WordStateLearn());
        word2.setState(new WordStateGSCorrect());
        Word word3 = new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build();
        word3.setState(new WordStateNew());
        word3.setState(new WordStateLearn());
        word3.setState(new WordStateSGCorrect());
        Word word4 = new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build();
        word4.setState(new WordStateNew());
        word4.setState(new WordStateLearn());

        words.addWord(word1);
        words.addWord(word2);
        words.addWord(word3);
        words.addWord(word4);

        words.clearListFromKnownWords();
        assertEquals(3, words.size());
        assertFalse(word1.getState() instanceof WordStateCorrect);
        assertTrue(word2.getState() instanceof WordStateGSCorrect && word3.getState() instanceof WordStateSGCorrect && word4.getState() instanceof WordStateLearn);
    }

    @Test
    public void testPrintResult() throws IllegalStateTransitionException, WordAlreadyExistsException {
        LearnWordList words = new LearnWordList();

        Word word1 = new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build();
        word1.setState(new WordStateNew());
        word1.setState(new WordStateLearn());
        word1.setState(new WordStateSGCorrect());
        word1.setState(new WordStateCorrect());
        Word word2 = new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build();
        word2.setState(new WordStateNew());
        word2.setState(new WordStateLearn());
        word2.setState(new WordStateGSCorrect());
        Word word3 = new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build();
        word3.setState(new WordStateNew());
        word3.setState(new WordStateLearn());
        word3.setState(new WordStateSGCorrect());
        Word word4 = new WordBuilder().addGerman("drei").addSwedish("tre", WordPrefix.NONE).build();
        word4.setState(new WordStateNew());
        word4.setState(new WordStateLearn());
        word4.setState(new WordStateSGCorrect());
        Word word5 = new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build();
        word5.setState(new WordStateNew());
        word5.setState(new WordStateLearn());

        words.addWord(word1);
        words.addWord(word2);
        words.addWord(word3);
        words.addWord(word4);
        words.addWord(word5);

        String expected = "Total Number of Words: 5\n" +
                "Correct (Swedish-German): 3 "+String.valueOf(Character.toChars(0x1F603))+"\n" +
                "Correct (German-Swedish): 2 "+String.valueOf(Character.toChars(0x1F61E))+"\n" +
                "\n" +
                "Still some work to do "+String.valueOf(Character.toChars(0x1F61E));
        assertEquals(expected, words.printResults());
    }
}