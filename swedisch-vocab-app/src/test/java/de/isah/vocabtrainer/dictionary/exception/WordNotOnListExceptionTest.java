package de.isah.vocabtrainer.dictionary.exception;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordNotOnListExceptionTest {

    @Test
    public void testExceptionOne(){
        WordNotOnListException e = new WordNotOnListException("word not on list");
        assertEquals("word not on list", e.getMessage());
    }

    @Test
    public void testExceptionTwo(){
        WordNotOnListException e = new WordNotOnListException("word not on list", new Throwable());
        assertEquals("word not on list", e.getMessage());
    }
}