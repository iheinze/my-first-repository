package de.isah.vocabtrainer.dictionary.constants;

/**
 * Constants related to files which are needed throughout the app
 *
 * Created by isa.heinze on 10.01.2018.
 */

public class FileConstants {

    private static String filePath;
    private static String fileExternalPath;

    public static void setFilePath(String currentFilePath){
        filePath = currentFilePath;
    }

    public static String getFilePath(){
        return filePath;
    }

    public static void setExternalFilePath(String currentFilePath){
        fileExternalPath = currentFilePath;
    }

    public static String getExternalFilePath(){
        return fileExternalPath;
    }

}
