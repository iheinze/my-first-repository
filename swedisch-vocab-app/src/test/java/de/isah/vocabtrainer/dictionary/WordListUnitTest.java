package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testDoesContainTrue() throws WordAlreadyExistsException {

        Word w = new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build();

        WordList words = new WordList();
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
        words.addWord(w);
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());

        assertTrue(words.doesContain(w));
    }

    @Test
    public void testDoesContainFalse() throws WordAlreadyExistsException {

        Word w = new WordBuilder().addGerman("Mädchen").addSwedish("flicka", WordPrefix.NONE).build();

        WordList words = new WordList();
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());

        assertFalse(words.doesContain(w));
    }

    @Test
    public void testGetWordKey() throws WordAlreadyExistsException{

        Word w = new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build();
        String key = w.getKey();
        WordList words = new WordList();
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());
        assertEquals(words.getWord(key).printGerman(), "Stift");
        assertEquals(words.getWord(key).printSwedishAndGrammar(), "penna");
    }

    @Test(expected = WordAlreadyExistsException.class)
    public void testAddWordException() throws WordAlreadyExistsException {
        WordList words = new WordList();
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
    }

    @Test
    public void testGetOriginalList() throws WordAlreadyExistsException {
        WordList words = new WordList();
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());
        assertEquals(words.getOriginalList().size(), 4);
    }

    @Test
    public void testRemoveWord() throws WordAlreadyExistsException {
        Word w = new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build();
        WordList words = new WordList();
        words.addWord(w);
        words.addWord(new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());
        words.removeWord(w);
        assertEquals(words.getOriginalList().size(), 3);
    }

    @Test
    public void testRemoveNotExistingWord() throws WordAlreadyExistsException {
        Word w = new WordBuilder().addGerman("Mädchen").addSwedish("flicka", WordPrefix.NONE).build();
        WordList words = new WordList();
        words.addWord(new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());
        words.removeWord(w);
        assertEquals(words.getOriginalList().size(), 4);
    }

    @Test
    public void testRemoveWords() throws WordAlreadyExistsException {
        Word w1 = new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build();
        Word w2 = new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build();

        List<Word> list = new ArrayList<>();
        list.add(w1);
        list.add(w2);

        WordList words = new WordList();
        words.addWord(w1);
        words.addWord(w2);
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());

        words.removeWords(list);

        assertEquals(words.getOriginalList().size(), 2);
    }

    @Test
    public void testRemoveNotExistingWords() throws WordAlreadyExistsException {
        Word w1 = new WordBuilder().addGerman("Hallo").addSwedish("hej", WordPrefix.NONE).build();
        Word w2 = new WordBuilder().addGerman("Junge").addSwedish("pojke", WordPrefix.NONE).build();
        Word w3 = new WordBuilder().addGerman("test").addSwedish("test", WordPrefix.NONE).build();
        Word w4 = new WordBuilder().addGerman("bla").addSwedish("bla", WordPrefix.NONE).build();

        List<Word> list = new ArrayList<>();
        list.add(w1);
        list.add(w2);
        list.add(w3);
        list.add(w4);

        WordList words = new WordList();
        words.addWord(w1);
        words.addWord(w2);
        words.addWord(new WordBuilder().addGerman("Stift").addSwedish("penna", WordPrefix.NONE).build());
        words.addWord(new WordBuilder().addGerman("gut").addSwedish("bra", WordPrefix.NONE).build());

        words.removeWords(list);

        assertEquals(words.getOriginalList().size(), 2);
    }
}