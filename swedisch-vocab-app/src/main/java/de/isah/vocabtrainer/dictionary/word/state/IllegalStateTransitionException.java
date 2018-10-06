package de.isah.vocabtrainer.dictionary.word.state;

/**
 * Created by isa.heinze on 09.05.2018.
 */

public class IllegalStateTransitionException extends Exception {

    public IllegalStateTransitionException(String message, Throwable t){
        super(message, t);
    }

    public IllegalStateTransitionException(String message){
        super(message);
    }
}
