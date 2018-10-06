package de.isah.vocabtrainer.dictionary.word.state;

/**
 * Created by isa.heinze on 03.05.2018.
 */

public abstract class WordState {

    public abstract boolean enableAddToNewList();

    public abstract boolean enableDeleteFromNewList();

    public abstract boolean showAddToNewList();

    public abstract boolean showDeleteFromNewList();

    public String getWordListsNames(){
        return "dictionary list";
    }

    public abstract String getWordListSymbol();

    public String getName(){
        return this.getClass().getSimpleName();
    }
}
