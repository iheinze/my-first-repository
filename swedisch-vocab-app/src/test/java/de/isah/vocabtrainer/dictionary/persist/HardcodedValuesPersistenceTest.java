package de.isah.vocabtrainer.dictionary.persist;

import org.junit.Test;

import de.isah.vocabtrainer.dictionary.WordList;
import de.isah.vocabtrainer.dictionary.word.Word;

import static org.junit.Assert.*;

public class HardcodedValuesPersistenceTest {

    @Test
    public void getAllWords(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertEquals(6, persistence.getAllWords().size());
    }

    @Test
    public void getNewWords(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertEquals(0, persistence.getNewWords().size());
    }

    @Test
    public void getLearnWords(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertEquals(0, persistence.getToLearnWords().size());
    }

    @Test
    public void getIncompleteWords(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertEquals(0, persistence.getIncompleteList().size());
    }

    @Test
    public void getAddWord(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertTrue(persistence.addWord(new Word()));
        assertEquals(6, persistence.getAllWords().size());
    }

    @Test
    public void getPersistAll(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertFalse(persistence.persistAll(new WordList()));
    }

    @Test
    public void getExportAll(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertFalse(persistence.exportAll(new WordList()));
    }

    @Test
    public void getImportFile(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertFalse(persistence.importFile());
    }

    @Test
    public void getDisableAddWords(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertFalse(persistence.disableAddWords());
    }

    @Test
    public void getDisableImportExport(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertTrue(persistence.disableImportExport());
    }

    @Test
    public void getDisableDeleteWords(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertFalse(persistence.disableDeleteWords());
    }

    @Test
    public void getDisableEditWords(){
        HardcodedValuesPersistence persistence = new HardcodedValuesPersistence();
        assertFalse(persistence.disableEditWords());
    }
}