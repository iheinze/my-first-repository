package de.isah.vocabtrainer.dictionary.word.state;

/**
 * Created by isa.heinze on 03.05.2018.
 */

public class WordStateNew extends WordState {

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
        return false;
    }

    @Override
    public boolean showDeleteFromNewList() {
        return true;
    }

    @Override
    public String getWordListsNames(){
        return super.getWordListsNames() + ", new words list";
    }

    @Override
    public String getWordListSymbol() {
        return String.valueOf(Character.toChars(0x1F4D7));
    }
}
