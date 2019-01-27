package de.isah.vocabtrainer.dictionary.word.state;

import org.junit.Test;

import static org.junit.Assert.*;

public class IllegalStateTransitionExceptionTest {

    @Test
    public void testExceptionOne(){
        IllegalStateTransitionException e = new IllegalStateTransitionException("wrong state");
        assertEquals("wrong state", e.getMessage());
    }

    @Test
    public void testExceptionTwo(){
        IllegalStateTransitionException e = new IllegalStateTransitionException("wrong state", new Throwable());
        assertEquals("wrong state", e.getMessage());
    }
}