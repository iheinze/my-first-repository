package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.word.Word;

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

    public int giveMeListLength(){
        return this.toLearnList.size();
    }

    public void reset(){
        this.counter = 0;
        this.currentWord = null;
    }
}
