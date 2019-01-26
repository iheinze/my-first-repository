package de.isah.vocabtrainer.dictionary.persist;

import de.isah.vocabtrainer.dictionary.persist.filehandling.AbstractFileHandler;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;

import org.json.JSONException;
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
    public void testPersist() throws UnsupportedEncodingException, JSONException {
        String testDir = "{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}--{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(2, persistence.getAllWords().size());
        assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}--", persistence.getAllWords().getWord(0).serialize());
        assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}--", persistence.getAllWords().getWord(1).serialize());

        assertEquals(0, persistence.getNewWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
    }

    @Test
    public void testNewLine() throws UnsupportedEncodingException, JSONException {
        String testDir = "{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}--\n{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(2, persistence.getAllWords().size());
        assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}--", persistence.getAllWords().getWord(0).serialize());
        assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}--", persistence.getAllWords().getWord(1).serialize());

        assertEquals(0, persistence.getNewWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
    }

    @Test
    public void testComment() throws UnsupportedEncodingException, JSONException {
        String testDir = "#{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}--{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(1, persistence.getAllWords().size());
        assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}--", persistence.getAllWords().getWord(0).serialize());

    }

    @Test
    public void testPersistInputNull(){
        AbstractFileHandler.setFileInStream(null);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(0, persistence.getAllWords().size());
        assertEquals(0, persistence.getNewWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
    }
}
