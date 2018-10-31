package de.isah.vocabtrainer.dictionary.exception;

public class WordNotOnListException extends Exception {

    public WordNotOnListException(String message, Throwable t){
        super(message, t);
    }

    public WordNotOnListException(String message){
        super(message);
    }

}
