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

    private static final int ROTATE_FILE_SIZE_MB = 1;

    private static boolean isDebugMode = false;

    @Deprecated
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
                System.out.println(log);
            } catch (IOException e){
                // do nothing
            }
        }
    }

    public static void log(final String message, final Class loggingClazz){
        if (isDebugMode){
            try {
                if(fileRotateNeeded()) {
                    rotateFile();
                }
                String log = getCurrentTimeStamp() + " : " + loggingClazz.getSimpleName() + " - " + message + "\n";
                FileOutputStream fileOutStream = new FileOutputStream(new File(FileConstants.getExternalFilePath() + "/" + LOG_FILE_NAME), true);
                fileOutStream.write(log.getBytes());
                fileOutStream.close();
                System.out.println(log);
            } catch (IOException e){
                // do nothing
            }
        }
    }

    public static void setIsDebugMode(boolean debugMode) {
        isDebugMode = debugMode;
    }

    private static void rotateFile() {
        boolean wasRenamed = new File(FileConstants.getExternalFilePath() + "/" + LOG_FILE_NAME).renameTo(new File(FileConstants.getExternalFilePath() + "/" + LOG_FILE_NAME_ROTATED));
    }

    private static boolean fileRotateNeeded() {
        File f = new File(FileConstants.getExternalFilePath() + "/" + LOG_FILE_NAME);
        return f.length() / 1024 / 1024 >= ROTATE_FILE_SIZE_MB;
    }

    static void deleteLogFile(){
        boolean wasDeleted1 = new File(FileConstants.getExternalFilePath() + "/" + LOG_FILE_NAME).delete();
        boolean wasDeleted2 = new File(FileConstants.getExternalFilePath() + "/" + LOG_FILE_NAME_ROTATED).delete();
        if(!wasDeleted1 | !wasDeleted2){
            //
        }
    }

    private static String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.GERMANY).format(new Date());
    }
}
