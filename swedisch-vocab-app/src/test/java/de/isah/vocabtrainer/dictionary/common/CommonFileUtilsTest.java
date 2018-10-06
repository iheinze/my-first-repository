package de.isah.vocabtrainer.dictionary.common;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

/**
 * Created by isa.heinze on 05.03.2018.
 */

public class CommonFileUtilsTest {

    @Test
    public void testCopyFile() throws IOException{
        File in = new File("testfile-in.txt");
        in.createNewFile();
        PrintWriter writer = new PrintWriter(in);
        writer.print("Hello World");
        writer.close();
        File out = new File("testfile-out.txt");
        out.createNewFile();

        assertEquals(0, out.length());
        CommonFileUtils.copyFile(in, out);
        assertEquals(11, out.length());

        in.delete();
        out.delete();
    }
}
