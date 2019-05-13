package de.isah.vocabtrainer;

import org.junit.Test;

import java.io.IOException;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.constants.FileConstants;

import static org.junit.Assert.*;

public class DebugInfoHandlerTest {

    @Test
    public void testGetDebugInfo() throws IOException {
        DebugInfoHandler handler = new DebugInfoHandler();
        Dictionary dict = new Dictionary("x");
        FileConstants.setExternalFilePath("src/test/assets");
        FileConstants.setFilePath("src/test/assets");
        String result = handler.getDebugInfo(dict, "AppName", "0.0.00-DEBUG");
        assertEquals("---DEBUG---\n" +
                "\n" +
                "App Name: AppName\n" +
                "App Version: 0.0.00-DEBUG\n" +
                "\n" +
                "Persistence type: HardcodedValuesPersistence\n" +
                "\n" +
                "Number of words in dictionary: 6\n" +
                "Number of new words: 0\n" +
                "Number of words in toLearn List: 0\n" +
                "Number of words in incomplete List: 0\n" +
                "\n" +
                "File Path: src/test/assets\n" +
                "External File Path: src/test/assets\n", result);
    }

}