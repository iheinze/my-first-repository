package de.isah.vocabtrainer.dictionary.word.state;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordStateDictionaryTest {

    private WordStateDictionary state = new WordStateDictionary();

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
        assertTrue(state.showAddToNewList());
    }

    @Test
    public void testShowDeleteFromNew(){
        assertFalse(state.showDeleteFromNewList());
    }

    @Test
    public void testGetWordListNames(){
        assertEquals("dictionary list", state.getWordListsNames());
    }

    @Test
    public void testGetWordListSymbol(){
        assertEquals("", state.getWordListSymbol());
    }

}