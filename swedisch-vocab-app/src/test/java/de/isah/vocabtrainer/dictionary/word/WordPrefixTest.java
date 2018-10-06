package de.isah.vocabtrainer.dictionary.word;

import de.isah.vocabtrainer.dictionary.word.WordPrefix;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 04.03.2018.
 */

public class WordPrefixTest {

    @Test
    public void testEn(){
        assertEquals("en", WordPrefix.EN.string);
        assertEquals("en ", WordPrefix.EN.start);
        assertEquals(", en", WordPrefix.EN.end);
        assertEquals("(en)\n\t", WordPrefix.EN.replace);
        assertEquals(3, WordPrefix.EN.length);
    }

    @Test
    public void testEtt(){
        assertEquals("ett", WordPrefix.ETT.string);
        assertEquals("ett ", WordPrefix.ETT.start);
        assertEquals(", ett", WordPrefix.ETT.end);
        assertEquals("(ett)\n\t", WordPrefix.ETT.replace);
        assertEquals(4, WordPrefix.ETT.length);
    }

    @Test
    public void testAtt(){
        assertEquals("att", WordPrefix.ATT.string);
        assertEquals("att ", WordPrefix.ATT.start);
        assertEquals(", att", WordPrefix.ATT.end);
        assertEquals("(att)\n\t", WordPrefix.ATT.replace);
        assertEquals(4, WordPrefix.ATT.length);
    }

    @Test
    public void testNone(){
        assertEquals("none", WordPrefix.NONE.string);
        assertEquals("", WordPrefix.NONE.start);
        assertEquals("", WordPrefix.NONE.end);
        assertEquals("", WordPrefix.NONE.replace);
        assertEquals(0, WordPrefix.NONE.length);
    }
}
