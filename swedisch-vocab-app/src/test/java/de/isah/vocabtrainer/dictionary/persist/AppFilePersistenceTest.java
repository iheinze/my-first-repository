package de.isah.vocabtrainer.dictionary.persist;

import de.isah.vocabtrainer.dictionary.word.WordBuilder;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 09.01.2018.
 */

public class AppFilePersistenceTest {

    @Test
    public void testPersist() throws UnsupportedEncodingException{
        String testDir = "none;med;mit;;;WordStateDictionary--en;bil;Auto;bilen,bilar,bilarna;;WordStateDictionary--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(2, persistence.getAllWords().size());
        assertEquals("none;med;mit;;;WordStateDictionary--", persistence.getAllWords().getWord(0).serialize());
        assertEquals("en;bil;Auto;bilen,bilar,bilarna;;WordStateDictionary--", persistence.getAllWords().getWord(1).serialize());

        assertEquals(0, persistence.getNewWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
    }

    @Test
    public void testNewLine() throws UnsupportedEncodingException{
        String testDir = "none;med;mit;;;WordStateDictionary--\nen;bil;Auto;bilen,bilar,bilarna;;WordStateDictionary--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(2, persistence.getAllWords().size());
        assertEquals("none;med;mit;;;WordStateDictionary--", persistence.getAllWords().getWord(0).serialize());
        assertEquals("en;bil;Auto;bilen,bilar,bilarna;;WordStateDictionary--", persistence.getAllWords().getWord(1).serialize());

        assertEquals(0, persistence.getNewWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
    }

    @Test
    public void testComment() throws UnsupportedEncodingException{
        String testDir = "#none;med;mit;;;WordStateDictionary--en;bil;Auto;bilen,bilar,bilarna;;WordStateDictionary--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(1, persistence.getAllWords().size());
        assertEquals("en;bil;Auto;bilen,bilar,bilarna;;WordStateDictionary--", persistence.getAllWords().getWord(0).serialize());
    }

    @Test
    public void testPersistInputNull() throws UnsupportedEncodingException{
        AbstractFileHandler.setFileInStream(null);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(0, persistence.getAllWords().size());
        assertEquals(0, persistence.getNewWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
    }
}
