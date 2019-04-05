package de.isah.vocabtrainer.dictionary;

import org.junit.Test;

import java.io.IOException;

import de.isah.vocabtrainer.dictionary.constants.FileConstants;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 09.01.2018.
 */

public class DictionaryCacheTest {

    @Test
    public void testGetDictionaryPersistentType(){
        Dictionary expected = DictionaryCache.getCachedDictionary("x");
        assertEquals(expected, DictionaryCache.getCachedDictionary("x"));
    }

    @Test
    public void testGetDictionary(){
        Dictionary expected = DictionaryCache.getCachedDictionary("x");
        assertEquals(expected, DictionaryCache.getCachedDictionary());
    }

    @Test
    public void testReloadDictionary(){
        Dictionary expected = DictionaryCache.getCachedDictionary("x");
        // a new Object should be created
        DictionaryCache.reloadDictionary("y");
        assertNotEquals(expected, DictionaryCache.getCachedDictionary());
    }

    @Test
    public void testReloadDictionaryException(){
        FileConstants.setFilePath("src/test/assets");
        FileConstants.setExternalFilePath("src/test/assets");
        Dictionary expected = DictionaryCache.getCachedDictionary("1");
        FileConstants.setFilePath("src/test/test/bla");
        FileConstants.setExternalFilePath("src/test/assets/bla");
        assertFalse(DictionaryCache.reloadDictionary("1"));
    }
}
