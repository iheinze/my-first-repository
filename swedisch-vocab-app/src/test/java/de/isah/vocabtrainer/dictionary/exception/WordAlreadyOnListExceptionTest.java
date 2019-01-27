package de.isah.vocabtrainer.dictionary.exception;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordAlreadyOnListExceptionTest {

    @Test
    public void testExceptionOne(){
        WordAlreadyOnListException e = new WordAlreadyOnListException("word on list");
        assertEquals("word on list", e.getMessage());
    }

    @Test
    public void testExceptionTwo(){
        WordAlreadyOnListException e = new WordAlreadyOnListException("word on list", new Throwable());
        assertEquals("word on list", e.getMessage());
    }

}