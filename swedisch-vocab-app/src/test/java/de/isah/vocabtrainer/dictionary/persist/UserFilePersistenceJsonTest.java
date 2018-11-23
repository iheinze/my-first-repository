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
        File inFile = new File(FileConstants.getFilePath()+ "/dictionary1json.txt");
        File outFile = new File(FileConstants.getFilePath()+ "/dictionaryjson.txt");

        CommonFileUtils.copyFile(inFile, outFile);

        UserFilePersistenceJson persistence = new UserFilePersistenceJson();
        assertEquals(4,persistence.getAllWords().size());
        assertEquals(1,persistence.getNewWords().size());
        assertEquals(1,persistence.getToLearnWords().size());

    }

    @Test
    public void testAddWord() throws IOException, IllegalStateTransitionException, JSONException {
        FileConstants.setFilePath("src/test/assets");
        File inFile = new File(FileConstants.getFilePath()+ "/dictionary1json.txt");
        File outFile = new File(FileConstants.getFilePath()+ "/dictionaryjson.txt");

        CommonFileUtils.copyFile(inFile, outFile);

        UserFilePersistenceJson persistence = new UserFilePersistenceJson();
        Word w = new WordBuilder().addSwedish("test1", WordPrefix.NONE).addGerman("test2").build();
        System.out.println(w.serializeToJsonString());
        w.setState(new WordStateNew());
        persistence.addWord(w);

        UserFilePersistenceJson persistence2 = new UserFilePersistenceJson();
        assertEquals(5,persistence2.getAllWords().size());
        assertEquals(2,persistence2.getNewWords().size());
        assertEquals(1,persistence2.getToLearnWords().size());

    }

    @Test
    public void testPersistWordList() throws IOException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        File outFile = new File(FileConstants.getFilePath()+ "/dictionaryjson.txt");
        boolean b = outFile.createNewFile();

        UserFilePersistenceJson persistence = new UserFilePersistenceJson();
        WordList list = new WordList();
        list.addWord(new WordBuilder().addSwedish("swedish1", WordPrefix.NONE).addGerman("german1").build());
        list.addWord(new WordBuilder().addSwedish("swedish2", WordPrefix.NONE).addGerman("german2").build());
        list.addWord(new WordBuilder().addSwedish("swedish3", WordPrefix.NONE).addGerman("german3").build());
        persistence.persistAll(list);

        UserFilePersistenceJson persistence2 = new UserFilePersistenceJson();
        assertEquals(3,persistence2.getAllWords().size());
        assertEquals(0,persistence2.getNewWords().size());
        assertEquals(0,persistence2.getToLearnWords().size());
    }

}