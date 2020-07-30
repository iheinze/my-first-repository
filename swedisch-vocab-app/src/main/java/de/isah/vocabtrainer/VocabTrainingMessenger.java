package de.isah.vocabtrainer;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.LearnWordList;
import de.isah.vocabtrainer.dictionary.word.Word;

public class VocabTrainingMessenger {

    private Dictionary dictionary;
    private LearnWordList toLearnList;
    private int counter;
    private Word currentWord = null;

    public VocabTrainingMessenger(){
        this.dictionary = DictionaryCache.getCachedDictionary();
        this.toLearnList = dictionary.getToLearnList();
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

    public void reset(){
        this.counter = 0;
    }
}
