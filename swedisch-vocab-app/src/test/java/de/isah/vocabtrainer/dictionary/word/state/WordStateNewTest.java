package de.isah.vocabtrainer.dictionary.word.state;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordStateNewTest {

    private WordStateNew state = new WordStateNew();

    @Test
    public void testEnableAddToNew(){
        assertTrue(state.enableAddToNewList());
    }

    @Test
    public void testEnableDeleteFromNew(){
        assertTrue(state.enableDeleteFromNewList());
    }

    @Test
    public void testShowAddToNew(){
        assertFalse(state.showAddToNewList());
    }

    @Test
    public void testShowDeleteFromNew(){
        assertTrue(state.showDeleteFromNewList());
    }

    @Test
    public void testGetWordListNames(){
        assertEquals("dictionary list, new words list", state.getWordListsNames());
    }

    @Test
    public void testGetWordListSymbol(){
        assertEquals("\uD83D\uDCD7", state.getWordListSymbol());
    }

}