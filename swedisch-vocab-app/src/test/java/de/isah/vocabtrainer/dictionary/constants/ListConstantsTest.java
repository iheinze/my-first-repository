package de.isah.vocabtrainer.dictionary.constants;

import org.junit.Test;

import static org.junit.Assert.*;

public class ListConstantsTest {

    @Test
    public void testPosition(){
        ListConstants.setPosition(5);
        assertEquals(5, ListConstants.getPosition());
    }

    @Test
    public void testConstraint(){
        ListConstants.setConstraint("constraint");
        assertEquals("constraint", ListConstants.getConstraint());
    }

}