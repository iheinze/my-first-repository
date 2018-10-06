package de.isah.vocabtrainer.dictionary.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class contains common utils for file handling
 *
 * Created by isa.heinze on 05.03.2018.
 */

public class CommonFileUtils {

    private CommonFileUtils(){}

    /**
     * Copy a file to another file
     *
     * @param inFile file to copy from
     * @param outFile file to copy to
     * @throws IOException exception
     */
    public static void copyFile(File inFile, File outFile) throws IOException {
        InputStream is = new FileInputStream(inFile);
        OutputStream os = new FileOutputStream(outFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }
}
