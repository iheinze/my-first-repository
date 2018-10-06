package de.isah.vocabtrainer;

import de.isah.vocabtrainer.dictionary.persist.AbstractFileHandler;
import de.isah.vocabtrainer.dictionary.persist.EmptyFileHandler;

/**
 * Created by isa.heinze on 08.07.2018.
 */

public class FileHandlerFactory {

    public static AbstractFileHandler create(String persistenceType){
        switch (persistenceType){
            case "1":
                return new EmptyFileHandler();
            case "2":
                return new AndroidResourceFileHandler();
            case "3":
                return new EmptyFileHandler();
            default:
                return new EmptyFileHandler();
        }
    }
}
