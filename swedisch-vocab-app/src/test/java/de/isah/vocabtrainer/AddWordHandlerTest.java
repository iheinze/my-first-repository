package de.isah.vocabtrainer;

import org.junit.Test;

import java.io.IOException;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;

import static org.junit.Assert.*;

public class AddWordHandlerTest {

    @Test
    public void testAddWordMin() throws IOException, IllegalStateTransitionException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        Dictionary dict = new Dictionary("x");

        AddWordHandler handler = new AddWordHandler();
        handler.addWord(dict, "swedish", "german", WordPrefix.NONE, "", "", false, ",", false);

        assertEquals(7, dict.getNWordsInDict());
        assertTrue(dict.getAllWordsList().getWord("swedish").getState() instanceof WordStateNew);
        assertEquals("swedish", dict.getAllWordsList().getWord("swedish").getSwedish());
        assertEquals("NONE", dict.getAllWordsList().getWord("swedish").getPrefix().toString());
        assertEquals("", dict.getAllWordsList().getWord("swedish").printRemark());
        assertEquals("german", dict.getAllWordsList().getWord("swedish").printGerman());
        assertEquals("", dict.getAllWordsList().getWord("swedish").printGrammar());
    }

    @Test
    public void testAddWordFull() throws IOException, IllegalStateTransitionException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        Dictionary dict = new Dictionary("x");

        AddWordHandler handler = new AddWordHandler();
        handler.addWord(dict, "swedish", "german1,german2", WordPrefix.EN, "swedish1,swedish2,swedish3", "remark", false, ",", false);

        assertEquals(7, dict.getNWordsInDict());
        assertTrue(dict.getAllWordsList().getWord("swedish, en").getState() instanceof WordStateNew);
        assertEquals("swedish", dict.getAllWordsList().getWord("swedish, en").getSwedish());
        assertEquals("EN", dict.getAllWordsList().getWord("swedish, en").getPrefix().toString());
        assertEquals("remark", dict.getAllWordsList().getWord("swedish, en").printRemark());
        assertEquals("german1, german2", dict.getAllWordsList().getWord("swedish, en").printGerman());
        assertEquals("swedish1, swedish2, swedish3", dict.getAllWordsList().getWord("swedish, en").printGrammar());
    }

    @Test
    public void testAddWordIncomplete() throws IOException, IllegalStateTransitionException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        Dictionary dict = new Dictionary("x");

        AddWordHandler handler = new AddWordHandler();
        handler.addWord(dict, "swedish", "german", WordPrefix.NONE, "", "", true, ",", false);

        assertEquals(7, dict.getNWordsInDict());
        assertTrue(dict.getAllWordsList().getWord("swedish").getState() instanceof WordStateIncomplete);
        assertEquals("swedish", dict.getAllWordsList().getWord("swedish").getSwedish());
        assertEquals("NONE", dict.getAllWordsList().getWord("swedish").getPrefix().toString());
        assertEquals("", dict.getAllWordsList().getWord("swedish").printRemark());
        assertEquals("german", dict.getAllWordsList().getWord("swedish").printGerman());
        assertEquals("", dict.getAllWordsList().getWord("swedish").printGrammar());
    }
}