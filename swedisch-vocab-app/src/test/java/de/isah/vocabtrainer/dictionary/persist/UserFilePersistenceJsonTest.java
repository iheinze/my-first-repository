package de.isah.vocabtrainer.dictionary.persist;

import org.json.JSONException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import de.isah.vocabtrainer.dictionary.WordList;
import de.isah.vocabtrainer.dictionary.common.CommonFileUtils;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;
import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;

import static org.junit.Assert.*;

public class UserFilePersistenceJsonTest {

    @Test
    public void testReadFile() throws IOException {
        FileConstants.setFilePath("src/test/assets");
        File inFile = new File(FileConstants.getFilePath() + "/dictionary1json.txt");
        File outFile = new File(FileConstants.getFilePath() + "/dictionaryjson.txt");

        CommonFileUtils.copyFile(inFile, outFile);

        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        assertEquals(8, persistence.getAllWords().size());
        assertEquals(1, persistence.getNewWords().size());
        assertEquals(4, persistence.getToLearnWords().size());
        assertEquals(1, persistence.getIncompleteList().size());

    }

    @Test
    public void testAddWord() throws IOException, IllegalStateTransitionException, JSONException {
        FileConstants.setFilePath("src/test/assets");
        File inFile = new File(FileConstants.getFilePath() + "/dictionary1json.txt");
        File outFile = new File(FileConstants.getFilePath() + "/dictionaryjson.txt");

        CommonFileUtils.copyFile(inFile, outFile);

        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        Word w = new WordBuilder().addSwedish("test1", WordPrefix.NONE).addGerman("test2").build();
        System.out.println(w.serializeToJsonString());
        w.setState(new WordStateNew());
        persistence.addWord(w);

        UserFilePersistenceJson persistence2 = new UserFilePersistenceJson("dictionaryjson.txt");
        assertEquals(9, persistence2.getAllWords().size());
        assertEquals(2, persistence2.getNewWords().size());
        assertEquals(4, persistence2.getToLearnWords().size());
        assertEquals(1, persistence2.getIncompleteList().size());

    }

    @Test(expected = WordAlreadyExistsException.class)
    public void testPersistWordExists() throws IOException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        File outFile = new File(FileConstants.getFilePath() + "/dictionaryjson.txt");
        boolean b = outFile.createNewFile();

        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        WordList list = new WordList();
        list.addWord(new WordBuilder().addSwedish("swedish1", WordPrefix.NONE).addGerman("german1").build());
        list.addWord(new WordBuilder().addSwedish("swedish1", WordPrefix.NONE).addGerman("german1").build());
    }

    @Test
    public void testPersistWordList() throws IOException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        File outFile = new File(FileConstants.getFilePath() + "/dictionaryjson.txt");
        boolean b = outFile.createNewFile();

        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        WordList list = new WordList();
        list.addWord(new WordBuilder().addSwedish("swedish1", WordPrefix.NONE).addGerman("german1").build());
        list.addWord(new WordBuilder().addSwedish("swedish2", WordPrefix.NONE).addGerman("german2").build());
        list.addWord(new WordBuilder().addSwedish("swedish3", WordPrefix.NONE).addGerman("german3").build());
        persistence.persistAll(list);

        UserFilePersistenceJson persistence2 = new UserFilePersistenceJson("dictionaryjson.txt");
        assertEquals(3, persistence2.getAllWords().size());
        assertEquals(0, persistence2.getNewWords().size());
        assertEquals(0, persistence2.getToLearnWords().size());
        assertEquals(0, persistence2.getIncompleteList().size());
    }

    @Test
    public void testExportWordList() throws IOException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        FileConstants.setExternalFilePath("src/test/assets");

        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        WordList list = new WordList();
        list.addWord(new WordBuilder().addSwedish("swedish1", WordPrefix.NONE).addGerman("german1").build());
        list.addWord(new WordBuilder().addSwedish("swedish2", WordPrefix.NONE).addGerman("german2").build());
        list.addWord(new WordBuilder().addSwedish("swedish3", WordPrefix.NONE).addGerman("german3").build());
        boolean result = persistence.exportAll(list);

        assertTrue(result);

        File exportFile = new File(FileConstants.getExternalFilePath() + "/dictionary-export-json.txt");
        assertTrue(exportFile.exists());
        assertTrue(exportFile.isFile());
        assertTrue(exportFile.length() > 0);
    }

    @Test
    public void testImportWordList() throws IOException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        FileConstants.setExternalFilePath("src/test/assets");

        File inFile = new File(FileConstants.getExternalFilePath() + "/dictionary-export-json-valid.txt");
        File outFile = new File(FileConstants.getExternalFilePath() + "/dictionary-export-json.txt");

        CommonFileUtils.copyFile(inFile, outFile);

        UserFilePersistenceJson persistence3 = new UserFilePersistenceJson("dictionaryjson.txt");
        boolean result = persistence3.importFile();

        assertTrue(result);

        assertEquals(3, persistence3.getAllWords().size());
        assertEquals(0, persistence3.getNewWords().size());
        assertEquals(0, persistence3.getToLearnWords().size());
        assertEquals(0, persistence3.getIncompleteList().size());
    }

    @Test
    public void testImportWordListInvalid() throws IOException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        FileConstants.setExternalFilePath("src/test/assets");

        File inFile = new File(FileConstants.getExternalFilePath() + "/dictionary-export-json-invalid.txt");
        File outFile = new File(FileConstants.getExternalFilePath() + "/dictionary-export-json.txt");

        CommonFileUtils.copyFile(inFile, outFile);

        UserFilePersistenceJson persistence4 = new UserFilePersistenceJson("dictionaryjson.txt");
        boolean result = persistence4.importFile();

        assertTrue(result);

        assertEquals(2, persistence4.getAllWords().size());
        assertEquals(0, persistence4.getNewWords().size());
        assertEquals(0, persistence4.getToLearnWords().size());
        assertEquals(0, persistence4.getIncompleteList().size());
    }

    @Test
    public void testDisableAddWords() throws IOException {
        FileConstants.setFilePath("src/test/assets");
        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        assertFalse(persistence.disableAddWords());
    }

    @Test
    public void testDisableImportExport() throws IOException {
        FileConstants.setFilePath("src/test/assets");
        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        assertFalse(persistence.disableImportExport());
    }

    @Test
    public void testDisableDeleteWords() throws IOException {
        FileConstants.setFilePath("src/test/assets");
        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        assertFalse(persistence.disableDeleteWords());
    }

    @Test
    public void testDisableEditdWords() throws IOException {
        FileConstants.setFilePath("src/test/assets");
        UserFilePersistenceJson persistence = new UserFilePersistenceJson("dictionaryjson.txt");
        assertFalse(persistence.disableEditWords());
    }
}