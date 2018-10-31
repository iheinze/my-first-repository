package de.isah.vocabtrainer;

import de.isah.vocabtrainer.dictionary.word.WordPrefix;

/**
 * Created by isa.heinze on 30.04.2018.
 */

class AndroidSpinnerItemWordPrefixMapper {


    static WordPrefix mapSpinnerItemToWordPrefix(String item){
        WordPrefix prefix;
        switch (item){
            case "none":
                prefix = WordPrefix.NONE;
                break;
            case "att":
                prefix = WordPrefix.ATT;
                break;
            case "en":
                prefix = WordPrefix.EN;
                break;
            case "ett":
                prefix = WordPrefix.ETT;
                break;
            default:
                prefix = WordPrefix.NONE;
        }
        return prefix;
    }

    static int mapWordPrefixToSpinnerItem(WordPrefix prefix){
        switch (prefix.string){
            case "none":
                return 1;
            case "att":
                return 2;
            case "en":
                return 3;
            case "ett":
                return 4;
            default:
                return 1;
        }
    }
}

