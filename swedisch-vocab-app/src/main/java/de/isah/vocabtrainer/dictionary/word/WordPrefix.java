package de.isah.vocabtrainer.dictionary.word;

/**
 * Enum which holds information on prefixes
 *
 * Created by isa.heinze on 04.03.2018.
 */

public enum WordPrefix {
    EN ("en", "en ", ", en", "(en)\n\t", 3),
    ETT ("ett", "ett ",", ett", "(ett)\n\t", 4),
    ATT ("att", "att ", ", att", "(att)\n\t", 4),
    NONE("none","","","",0);

    public final String string;
    public final String start;
    public final String end;
    public final String replace;
    public final int length;

    WordPrefix(String string, String start, String end, String replace, int length){
        this.string = string;
        this.start = start;
        this.end = end;
        this.replace = replace;
        this.length = length;
    }

}
