package de.isah.vocabtrainer;

import android.content.res.AssetManager;

import de.isah.vocabtrainer.dictionary.persist.filehandling.AbstractFileHandler;

import java.io.IOException;

/**
 * Created by isa.heinze on 09.04.2018.
 */

public class AndroidResourceFileHandler extends AbstractFileHandler<AssetManager> {

    @Override
    public void openIniDictionaryFile(AssetManager assets){
        try {
            fileInStream = assets.open(AbstractFileHandler.INI_DICT_FILENAME);
        } catch (IOException e){
            // just do nothing
        }
    }

    @Override
    public void closeIniDictionary(){
        if(fileInStream != null) {
            try {
                fileInStream.close();

            } catch (IOException e) {
                // just do nothing
            }
        }
    }
}
