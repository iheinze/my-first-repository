package de.isah.vocabtrainer.logging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import de.isah.vocabtrainer.dictionary.constants.FileConstants;

import static org.junit.Assert.*;

public class SwedishVocabAppLoggerTest {

    @Before
    public void setup(){
        FileConstants.setExternalFilePath("src/test/assets");
    }

    @After
    public void cleanup(){
        SwedishVocabAppLogger.deleteLogFile();
    }

    @Test
    public void testLogOneLine() throws IOException{
        SwedishVocabAppLogger.log("this is a test message", SwedishVocabAppLoggerTest.class, true);

        List<String> lines = Files.readAllLines(Paths.get("src/test/assets/SwedishVocabAppDebugLog.txt"), Charset.defaultCharset());

        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("this is a test message"));
    }

    @Test
    public void testLogMultiLines() throws IOException{
        SwedishVocabAppLogger.log("line 1", SwedishVocabAppLoggerTest.class, true);
        SwedishVocabAppLogger.log("line 2", SwedishVocabAppLoggerTest.class, true);

        List<String> lines = Files.readAllLines(Paths.get("src/test/assets/SwedishVocabAppDebugLog.txt"), Charset.defaultCharset());

        assertEquals(2, lines.size());
    }

    @Test
    public void testLogNoDebug(){
        SwedishVocabAppLogger.log("this is a test message", SwedishVocabAppLoggerTest.class, false);
        assertFalse(new File("src/test/assets/SwedishVocabAppDebugLog.txt").exists());
    }

    @Test
    public void testFileRotate() {
        String log = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        for(int i = 0; i< 5500; i++) {
            SwedishVocabAppLogger.log(log, SwedishVocabAppLoggerTest.class, true);
        }
        assertTrue(new File("src/test/assets/SwedishVocabAppDebugLog1.txt").exists());
    }

    @Test
    public void testTwoFileRotates() {
        String log = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        for(int i = 0; i< 11000; i++) {
            SwedishVocabAppLogger.log(log, SwedishVocabAppLoggerTest.class, true);
        }
        assertTrue(new File("src/test/assets/SwedishVocabAppDebugLog1.txt").exists());
    }
}