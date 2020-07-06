package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

public class WordOfTheDay {

    private static final String NO_WORD_OF_THE_DAY = "No word of the day chosen";
    private static Word wordOfTheDay;

    public static void setWordOfTheDay(Word word){
        SwedishVocabAppLogger.log("Word of the day is set", WordOfTheDay.class);
        wordOfTheDay = word;
    }

    public static String printSwedishAndGrammar(){
        if(wordOfTheDay == null) {
            return NO_WORD_OF_THE_DAY;
        }
        return wordOfTheDay.printSwedishAndGrammar();
    }

    public static String printGerman(){
        if(wordOfTheDay == null) {
            return NO_WORD_OF_THE_DAY;
        }
        return wordOfTheDay.printGerman();
    }

    public static String printRemark(){
        if(wordOfTheDay == null) {
            return NO_WORD_OF_THE_DAY;
        }
        return wordOfTheDay.printRemark();
    }

    public static String toStringWidget(){
        if(wordOfTheDay == null) {
            return NO_WORD_OF_THE_DAY;
        }
        return wordOfTheDay.toStringMinimal();
    }
}
