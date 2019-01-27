package de.isah.vocabtrainer.dictionary.word.state;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordStateIncompleteTest {

    private WordStateIncomplete state = new WordStateIncomplete();

    @Test
    public void testEnableAddToNew(){
        assertFalse(state.enableAddToNewList());
    }

    @Test
    public void testEnableDeleteFromNew(){
        assertFalse(state.enableDeleteFromNewList());
    }

    @Test
    public void testShowAddToNew(){
        assertTrue(state.showAddToNewList());
    }

    @Test
    public void testShowDeleteFromNew(){
        assertFalse(state.showDeleteFromNewList());
    }

    @Test
    public void testGetWordListNames(){
        assertEquals("dictionary list, incomplete words list", state.getWordListsNames());
    }

    @Test
    public void testGetWordListSymbol(){
        assertEquals("\uD83D\uDD8B", state.getWordListSymbol());
    }

}