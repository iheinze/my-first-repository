package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.WordStateCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateGSCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 15.12.2017.
 */
public class WordListUnitTest {

    @Test
    public void testWordListSize() throws WordAlreadyExistsException {

        WordList words = new WordList();
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());
        assertEquals(words.size(), 4);
    }

    @Test
    public void testGetWord() throws WordAlreadyExistsException{
        WordList words = new WordList();
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());
        assertEquals(words.getWord(2).printGerman(), "Stift");
        assertEquals(words.getWord(2).printSwedishAndGrammar(), "penna");
    }

}