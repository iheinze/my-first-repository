package de.isah.vocabtrainer.dictionary.word.state;

/**
 * Created by isa.heinze on 03.05.2018.
 */

public class WordStateIncomplete extends WordState {

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
        return super.getWordListsNames() + ", incomplete words list";
    }

    @Override
    public String getWordListSymbol() {
        return String.valueOf(Character.toChars(0x1F58B));
    }
}
