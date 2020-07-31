package de.isah.vocabtrainer.dictionary.learn;

import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.LearnWordList;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;

public class VocabTrainingMessenger {

    private LearnWordList toLearnList;
    private int counter;
    private Word currentWord = null;

    public VocabTrainingMessenger(){
        this.toLearnList = DictionaryCache.getCachedDictionary().getToLearnList();
        this.counter = 0;
    }

    public Word giveMeNextWord(){
        this.currentWord = this.toLearnList.getWord(counter);
        this.counter++;
        return this.currentWord;
    }

    public Word giveMeCurrentWord(){
        return this.currentWord;
    }

    public int giveMeCurrentCounter(){
        return this.counter;
    }

    public int giveMeLearnListLength(){
        return this.toLearnList.size();
    }

    public void reset(){
        this.counter = 0;
        this.currentWord = null;
    }

    public void setCorrectState(LearnDirection direction){
        switch (direction) {
            case SG:
                try {
                    this.currentWord.setCorrectStateSG();
                    break;
                } catch (IllegalStateTransitionException e) {
                    break;
                }
            case GS:
                try {
                    this.currentWord.setCorrectStateGS();
                    break;
                } catch (IllegalStateTransitionException e) {
                    break;
                }
            default:
                break;
        }
    }

    public String giveMeFirstSide(LearnDirection direction) {
        switch (direction) {
            case SG:
                return this.currentWord.printSwedishAndGrammar();
            case GS:
                return this.currentWord.printGerman();
            default:
                return null;
        }
    }

    public String giveMeSecondSide(LearnDirection direction){
        switch (direction) {
            case SG:
                return this.currentWord.printGerman();
            case GS:
                return this.currentWord.printSwedishAndGrammar();
            default:
                return null;
        }
    }

    public String giveMeOtherSideButtonName(LearnDirection direction){
        switch (direction) {
            case SG:
                return"Show German";
            case GS:
                return "Show Swedish";
            default:
                return null;
        }
    }
}
