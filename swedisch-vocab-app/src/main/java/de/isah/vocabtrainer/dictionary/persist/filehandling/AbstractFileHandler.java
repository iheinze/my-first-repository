package de.isah.vocabtrainer.dictionary.persist.filehandling;

import java.io.InputStream;

/**
 * Created by isa.heinze on 09.04.2018.
 */

public abstract class AbstractFileHandler<T> {

    protected static String INI_DICT_FILENAME = "init-dictionary.txt";
    protected static InputStream fileInStream;

    abstract public void closeIniDictionary();

    abstract public void openIniDictionaryFile(T fileManager);

    public static InputStream getFileInStream(){
        return fileInStream;
    }

    public static void setFileInStream(InputStream stream){
        fileInStream = stream;
    }
}
