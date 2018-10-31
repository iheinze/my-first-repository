package de.isah.vocabtrainer.dictionary.persist.filehandling;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 14.04.2018.
 */

public class AbstractFileHandlerTest {

    @Test
    public void testAbstractFileHandler() throws UnsupportedEncodingException {
        InputStream testIs = new ByteArrayInputStream("testSTring".getBytes("UTF-8"));

        AbstractFileHandler.setFileInStream(testIs);

        assertEquals(testIs, AbstractFileHandler.getFileInStream());
    }
}
