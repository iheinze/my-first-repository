package de.isah.vocabtrainer.dictionary.word.state;

/**
 * Created by isa.heinze on 03.05.2018.
 */

public class WordStateInitial extends WordState {

    @Override
    public boolean enableAddToNewList() {
        return false;
    }

    @Override
    public boolean enableDeleteFromNewList() {
        return false;
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
    public String getWordListsNames(){
        return "";
    }

    @Override
    public String getWordListSymbol() {
        return String.valueOf(Character.toChars(0x26A0));
    }
}
