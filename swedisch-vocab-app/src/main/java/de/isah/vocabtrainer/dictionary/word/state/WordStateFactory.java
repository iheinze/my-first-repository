package de.isah.vocabtrainer.dictionary.word.state;

/**
 * Created by isa.heinze on 19.05.2018.
 */

public class WordStateFactory {

    private static WordStateInitial initialState = new WordStateInitial();
    private static WordStateIncomplete incompleteState = new WordStateIncomplete();
    private static WordStateNew newState = new WordStateNew();
    private static WordStateLearn learnState = new WordStateLearn();
    private static WordStateGSCorrect gsCorrectState = new WordStateGSCorrect();
    private static WordStateSGCorrect sgCorrectState = new WordStateSGCorrect();
    private static WordStateCorrect correctState = new WordStateCorrect();
    private static WordStateDictionary dictionaryState = new WordStateDictionary();

    public WordState create(String stateName){
        switch (stateName){
            case "WordStateInitial":
                return initialState;
            case "WordStateIncomplete":
                return incompleteState;
            case "WordStateNew":
                return newState;
            case "WordStateLearn":
                return learnState;
            case "WordStateGSCorrect":
                return gsCorrectState;
            case "WordStateSGCorrect":
                return sgCorrectState;
            case "WordStateCorrect" :
                return correctState;
            case "WordStateDictionary":
                return dictionaryState;
            default:
                return dictionaryState;
        }
    }
}
