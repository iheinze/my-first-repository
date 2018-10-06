package de.isah.vocabtrainer.dictionary.word.state;

/**
 * Created by isa.heinze on 03.05.2018.
 */

public class WordStateDictionary extends WordState {

    @Override
    public boolean enableAddToNewList() {
        return true;
    }

    @Override
    public boolean enableDeleteFromNewList() {
        return true;
    }

    @Override
    public boolean showAddToNewList() {
        return true;
    }

    @Override
    public boolean showDeleteFromNewList() {
        return false;
    }

    @Override
    public String getWordListSymbol() {
        return "";
    }
}
