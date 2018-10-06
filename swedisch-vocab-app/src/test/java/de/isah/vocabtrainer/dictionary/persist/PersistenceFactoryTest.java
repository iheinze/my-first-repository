package de.isah.vocabtrainer.dictionary.persist;

import de.isah.vocabtrainer.dictionary.constants.FileConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 09.01.2018.
 */

public class PersistenceFactoryTest {

    private File f;

    @Before
    public void setup() throws IOException {
        f = new File("dictionary3.txt");
        if(f.exists()){
            f.delete();
        }
        f.createNewFile();
    }

    @After
    public void cleanup() {
        f.delete();
    }

    @Test
    public void testFile() throws IOException{
        FileConstants.setFilePath(".");
        PersistenceFactory factory = new PersistenceFactory();
        assertTrue(factory.create("1") instanceof UserFilePersistence);
    }


    @Test
    public void testFileTwo() throws IOException{
        FileConstants.setFilePath(".");
        PersistenceFactory factory = new PersistenceFactory();
        assertTrue(factory.create("2") instanceof AppFilePersistence);
    }

    @Test
    public void testHardCoded() throws IOException{
        FileConstants.setFilePath(".");
        PersistenceFactory factory = new PersistenceFactory();
        assertTrue(factory.create("3") instanceof HardcodedValuesPersistence);
    }

    @Test
    public void testDefault() throws IOException{
        FileConstants.setFilePath(".");
        PersistenceFactory factory = new PersistenceFactory();
        assertTrue(factory.create("x") instanceof HardcodedValuesPersistence);
    }

}
