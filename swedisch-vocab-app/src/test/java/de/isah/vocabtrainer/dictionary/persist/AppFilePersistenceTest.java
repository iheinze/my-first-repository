package de.isah.vocabtrainer.dictionary.persist;

import de.isah.vocabtrainer.dictionary.WordList;
import de.isah.vocabtrainer.dictionary.persist.filehandling.AbstractFileHandler;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

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
        String result1 = persistence.getAllWords().getWord(0).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"source\":\"UNKNOWN\",\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}", result1.substring(0, result1.length() - 2), true);
        String result2 = persistence.getAllWords().getWord(1).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"source\":\"UNKNOWN\",\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}", result2.substring(0, result2.length() - 2), true);

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
        String result1 = persistence.getAllWords().getWord(0).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"source\":\"UNKNOWN\",\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}--", result1.substring(0, result1.length() - 2), true);
        String result2 = persistence.getAllWords().getWord(1).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"source\":\"UNKNOWN\",\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}", result2.substring(0, result2.length() - 2), true);

        assertEquals(0, persistence.getNewWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
        assertEquals(0, persistence.getIncompleteList().size());
    }

    @Test
    public void testComment() throws UnsupportedEncodingException, JSONException {
        String testDir = "#{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateDictionary\"}--{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(1, persistence.getAllWords().size());
        String result = persistence.getAllWords().getWord(0).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"source\":\"UNKNOWN\",\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateDictionary\"}", result.substring(0, result.length() - 2), true);
    }

    @Test
    public void testPersistInputNull(){
        AbstractFileHandler.setFileInStream(null);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(0, persistence.getAllWords().size());
        assertEquals(0, persistence.getNewWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
        assertEquals(0, persistence.getIncompleteList().size());
    }

    @Test
    public void testNewList() throws UnsupportedEncodingException, JSONException {
        String testDir = "{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateNew\"}--{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateNew\"}--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(2, persistence.getNewWords().size());
        String result1 = persistence.getNewWords().poll().serialize();
        JSONAssert.assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"source\":\"UNKNOWN\",\"prefix\":\"none\",\"state\":\"WordStateNew\"}", result1.substring(0, result1.length() - 2), true);
        String result2 = persistence.getNewWords().poll().serialize();
        JSONAssert.assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"source\":\"UNKNOWN\",\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateNew\"}", result2.substring(0, result2.length() - 2), true);

        assertEquals(2, persistence.getAllWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
        assertEquals(0, persistence.getIncompleteList().size());
    }

    @Test
    public void testIncompleteList() throws UnsupportedEncodingException, JSONException {
        String testDir = "{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateIncomplete\"}--{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateIncomplete\"}--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(2, persistence.getIncompleteList().size());
        String result1 = persistence.getIncompleteList().getWord(0).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"source\":\"UNKNOWN\",\"prefix\":\"none\",\"state\":\"WordStateIncomplete\"}", result1.substring(0, result1.length() - 2), true);
        String result2 = persistence.getIncompleteList().getWord(1).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"source\":\"UNKNOWN\",\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateIncomplete\"}", result2.substring(0, result2.length() - 2), true);

        assertEquals(2, persistence.getAllWords().size());
        assertEquals(0, persistence.getToLearnWords().size());
        assertEquals(0, persistence.getNewWords().size());
    }

    @Test
    public void testLearnList() throws UnsupportedEncodingException, JSONException {
        String testDir = "{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateLearn\"}--{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateLearn\"}--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(2, persistence.getToLearnWords().size());
        String result1 = persistence.getToLearnWords().getWord(0).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateLearn\",\"source\":\"UNKNOWN\"}", result1.substring(0, result1.length() - 2), true);
        String result2 = persistence.getToLearnWords().getWord(1).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"bil\",\"german\":[\"Auto\"],\"grammar\":[\"bilen\",\"bilar\",\"bilarna\"],\"prefix\":\"en\",\"state\":\"WordStateLearn\",\"source\":\"UNKNOWN\"}", result2.substring(0, result2.length() - 2), true);

        assertEquals(2, persistence.getAllWords().size());
        assertEquals(0, persistence.getIncompleteList().size());
        assertEquals(0, persistence.getNewWords().size());
    }

    @Test
    public void testWordExists() throws UnsupportedEncodingException, JSONException {
        String testDir = "{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateLearn\"}--{\"swedish\":\"med\",\"german\":[\"mit\"],\"prefix\":\"none\",\"state\":\"WordStateLearn\"}--";
        InputStream inStream = new ByteArrayInputStream(testDir.getBytes("UTF-8"));
        AbstractFileHandler.setFileInStream(inStream);
        AppFilePersistence persistence = new AppFilePersistence();
        persistence.addWord(new WordBuilder().build());

        assertEquals(1, persistence.getAllWords().size());
        String result = persistence.getToLearnWords().getWord(0).serialize();
        JSONAssert.assertEquals("{\"swedish\":\"med\",\"german\":[\"mit\"],\"source\":\"UNKNOWN\",\"prefix\":\"none\",\"state\":\"WordStateLearn\"}", result.substring(0, result.length() - 2), true);

    }

    @Test
    public void testPersistAll(){
        AppFilePersistence persistence = new AppFilePersistence();
        assertFalse(persistence.persistAll(new WordList()));
    }

    @Test
    public void testExportAll(){
        AppFilePersistence persistence = new AppFilePersistence();
        assertFalse(persistence.exportAll(new WordList()));
    }

    @Test
    public void testImportFile(){
        AppFilePersistence persistence = new AppFilePersistence();
        assertFalse(persistence.importFile());
    }

    @Test
    public void testDisableAddWord(){
        AppFilePersistence persistence = new AppFilePersistence();
        assertTrue(persistence.disableAddWords());
    }

    @Test
    public void testDisableImportExport(){
        AppFilePersistence persistence = new AppFilePersistence();
        assertTrue(persistence.disableImportExport());
    }

    @Test
    public void testDisableDeleteWords(){
        AppFilePersistence persistence = new AppFilePersistence();
        assertTrue(persistence.disableDeleteWords());
    }

    @Test
    public void testDisableEditWords(){
        AppFilePersistence persistence = new AppFilePersistence();
        assertTrue(persistence.disableEditWords());
    }
}
