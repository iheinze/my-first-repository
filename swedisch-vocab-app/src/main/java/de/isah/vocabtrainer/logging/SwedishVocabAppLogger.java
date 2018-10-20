package de.isah.vocabtrainer.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.isah.vocabtrainer.dictionary.constants.FileConstants;

public class SwedishVocabAppLogger {
    private static final String LOG_FILE_NAME = "SwedishVocabAppDebugLog.txt";
    private static final String LOG_FILE_NAME_ROTATED = "SwedishVocabAppDebugLog1.txt";

    public static void log(final String message, final Class loggingClazz, final boolean isDebugMode){
        if (isDebugMode){
            try {
                if(fileRotateNeeded()) {
                    rotateFile();
                }
                String log = getCurrentTimeStamp() + " : " + loggingClazz.getSimpleName() + " - " + message + "\n";
                FileOutputStream fileOutStream = new FileOutputStream(new File(FileConstants.getExternalFilePath() + "/" + LOG_FILE_NAME), true);
                fileOutStream.write(log.getBytes());
                fileOutStream.close();
            } catch (IOException e){
                // do nothing
            }
        }
    }

    private static void rotateFile() {
        // to be implemented
    }

    private static boolean fileRotateNeeded() {
        // to be implemented
        return false;
    }

    public static void deleteLogFile(){
        new File(FileConstants.getExternalFilePath() + "/" + LOG_FILE_NAME).delete();
    }

    private static String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.GERMANY).format(new Date());
    }
}
