package de.isah.vocabtrainer.dictionary.persist;

import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.WordList;
import de.isah.vocabtrainer.dictionary.common.CommonFileUtils;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 09.01.2018.
 */

public class UserFilePersistenceTest {

    @Test
    public void testReadFile() throws IOException{
        FileConstants.setFilePath("src/test/assets");
        File inFile = new File(FileConstants.getFilePath()+ "/dictionary1.txt");
        File outFile = new File(FileConstants.getFilePath()+ "/dictionary.txt");

        CommonFileUtils.copyFile(inFile, outFile);

        UserFilePersistence persistence = new UserFilePersistence();
        assertEquals(4,persistence.getAllWords().size());
        assertEquals(1,persistence.getNewWords().size());
        assertEquals(1,persistence.getToLearnWords().size());

    }

    @Test
    public void testAddWord() throws IOException,IllegalStateTransitionException{
        FileConstants.setFilePath("src/test/assets");
        File inFile = new File(FileConstants.getFilePath()+ "/dictionary1.txt");
        File outFile = new File(FileConstants.getFilePath()+ "/dictionary.txt");

        CommonFileUtils.copyFile(inFile, outFile);

        UserFilePersistence persistence = new UserFilePersistence();
        Word w = new WordBuilder().addSwedish("test1", WordPrefix.NONE).addGerman("test2").build();
        w.setState(new WordStateNew());
        persistence.addWord(w);

        UserFilePersistence persistence2 = new UserFilePersistence();
        assertEquals(5,persistence2.getAllWords().size());
        assertEquals(2,persistence2.getNewWords().size());
        assertEquals(1,persistence2.getToLearnWords().size());

    }

    @Test
    public void testPersistWordList() throws IOException, WordAlreadyExistsException {
        FileConstants.setFilePath("src/test/assets");
        File outFile = new File(FileConstants.getFilePath()+ "/dictionary.txt");
        outFile.createNewFile();

        UserFilePersistence persistence = new UserFilePersistence();
        WordList list = new WordList();
        list.addWord(new WordBuilder().addSwedish("swedish1", WordPrefix.NONE).addGerman("german1").build());
        list.addWord(new WordBuilder().addSwedish("swedish2", WordPrefix.NONE).addGerman("german2").build());
        list.addWord(new WordBuilder().addSwedish("swedish3", WordPrefix.NONE).addGerman("german3").build());
        persistence.persistAll(list);

        UserFilePersistence persistence2 = new UserFilePersistence();
        assertEquals(3,persistence2.getAllWords().size());
        assertEquals(0,persistence2.getNewWords().size());
        assertEquals(0,persistence2.getToLearnWords().size());
    }
}
