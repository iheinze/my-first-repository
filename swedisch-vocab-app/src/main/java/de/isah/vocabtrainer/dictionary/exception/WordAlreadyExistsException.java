package de.isah.vocabtrainer.dictionary.exception;

/**
 *
 */
public class WordAlreadyExistsException extends Exception {

    public WordAlreadyExistsException(String message, Throwable t){
        super(message, t);
    }

    public WordAlreadyExistsException(String message){
        super(message);
    }
}
