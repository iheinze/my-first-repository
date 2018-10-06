package de.isah.vocabtrainer.dictionary.constants;

/**
 * Constants related to word lists which are needed for listing details of words and return to the same position in the list afterwards.
 *
 * Created by isa.heinze on 09.03.2018.
 */

public class ListConstants {

    private static int position;
    private static String constraint;

    private ListConstants() {}

    public static void setPosition(int pos){
        position = pos;
    }

    public static int getPosition(){
        return position;
    }

    public static void setConstraint(String c){
        constraint = c;
    }

    public static String getConstraint(){
        return constraint;
    }
}
