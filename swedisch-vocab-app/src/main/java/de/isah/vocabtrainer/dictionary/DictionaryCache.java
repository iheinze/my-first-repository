package de.isah.vocabtrainer.dictionary;

import java.io.IOException;

/**
 * This class ensures that the Dictionary is a singleton.
 *
 * Created by isa.heinze on 02.01.2018.
 */

public class DictionaryCache {

    private static Dictionary dictionary;

    public static Dictionary getCachedDictionary(String persistenceType){

        if(dictionary != null){
            return dictionary;
        }

        reloadDictionary(persistenceType);

        return dictionary;
    }

    public static Dictionary getCachedDictionary(){
        return dictionary;
    }

    public static boolean reloadDictionary(String persistenceType){
        boolean returnValue;
        try {
            dictionary = new Dictionary(persistenceType);
            returnValue = true;
        } catch (IOException e) {
            e.printStackTrace();
            returnValue= false;
        }
        return returnValue;
    }
}
