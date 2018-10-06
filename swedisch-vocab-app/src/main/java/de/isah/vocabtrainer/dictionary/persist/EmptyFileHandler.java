package de.isah.vocabtrainer.dictionary.persist;

/**
 * A file handler which does nothing
 * Created by isa.heinze on 08.07.2018.
 */

public class EmptyFileHandler extends AbstractFileHandler {
    @Override
    public void closeIniDictionary() {

    }

    @Override
    public void openIniDictionaryFile(Object fileManager) {

    }
}
