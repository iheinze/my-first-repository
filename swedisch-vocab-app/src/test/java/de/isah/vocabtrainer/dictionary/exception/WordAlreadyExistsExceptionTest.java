package de.isah.vocabtrainer.dictionary.exception;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordAlreadyExistsExceptionTest {

    @Test
    public void testExceptionOne(){
        WordAlreadyExistsException e = new WordAlreadyExistsException("word does exist");
        assertEquals("word does exist", e.getMessage());
    }

    @Test
    public void testExceptionTwo(){
        WordAlreadyExistsException e = new WordAlreadyExistsException("word does exist", new Throwable());
        assertEquals("word does exist", e.getMessage());
    }
}