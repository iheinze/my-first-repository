package de.isah.vocabtrainer.dictionary;

import org.junit.Test;

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
}
