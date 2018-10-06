package de.isah.vocabtrainer.dictionary.word.state;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 19.05.2018.
 */
public class WordStateFactoryTest {

    @Test
    public void createWordStateInitial(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create(new WordStateInitial().getName());
        assertTrue(state instanceof WordStateInitial);
    }

    @Test
    public void createWordStateIncomplete(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create(new WordStateIncomplete().getName());
        assertTrue(state instanceof WordStateIncomplete);
    }

    @Test
    public void createWordStateNew(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create(new WordStateNew().getName());
        assertTrue(state instanceof WordStateNew);
    }

    @Test
    public void createWordStateLearn(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create(new WordStateLearn().getName());
        assertTrue(state instanceof WordStateLearn);
    }

    @Test
    public void createWordStateGSCorrect(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create(new WordStateGSCorrect().getName());
        assertTrue(state instanceof WordStateGSCorrect);
    }

    @Test
    public void createWordStateSGCorrect(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create(new WordStateSGCorrect().getName());
        assertTrue(state instanceof WordStateSGCorrect);
    }

    @Test
    public void createWordStateCorrect(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create(new WordStateCorrect().getName());
        assertTrue(state instanceof WordStateCorrect);
    }

    @Test
    public void createWordStateDictionary(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create(new WordStateDictionary().getName());
        assertTrue(state instanceof WordStateDictionary);
    }

    @Test
    public void createWordStateDefault(){
        WordStateFactory factory = new WordStateFactory();
        WordState state = factory.create("Default");
        assertTrue(state instanceof WordStateDictionary);
    }

    @Test
    public void checkSingletonDictionary(){
        WordStateFactory factory = new WordStateFactory();
        WordState state1 = factory.create(new WordStateDictionary().getName());
        WordState state2 = factory.create(new WordStateDictionary().getName());

        assertEquals(state1, state2);
    }

}