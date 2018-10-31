package de.isah.vocabtrainer.dictionary.exception;

public class WordAlreadyOnListException extends Exception {

    public WordAlreadyOnListException(String message, Throwable t){
        super(message, t);
    }

    public WordAlreadyOnListException(String message){
        super(message);
    }

}
