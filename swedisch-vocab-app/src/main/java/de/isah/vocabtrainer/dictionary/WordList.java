package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.exception.WordAlreadyExistsException;
import de.isah.vocabtrainer.dictionary.word.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author isa.heinze
 */
public class WordList implements Serializable{

    List<Word> words = new ArrayList<>();
    private Map<String,Word> wordsMap = new LinkedHashMap<>();

    public WordList(){

    }

    public int size(){
        return words.size();
        //return wordsMap.size();
    }

    boolean doesContain(Word w){
        return words.contains(w);
        //return wordsMap.containsValue(w);
    }

    public Word getWord(int i){
        return words.get(i);
        //return (new ArrayList<>(wordsMap.values())).get(i);
    }

    public Word getWord(String key){
        return wordsMap.get(key);
    }

    public void addWord(Word w) throws WordAlreadyExistsException {
        if(wordsMap.containsKey(w.getKey())){
            throw new WordAlreadyExistsException("This word or a similar one exists.");
        }
        words.add(w);
        wordsMap.put(w.getKey(), w);
    }

    public List<Word> getOriginalList(){
        return this.words;
        //return new ArrayList<>(wordsMap.values());
    }

    void removeWord(Word word){
        words.remove(word);
        wordsMap.remove(word.getKey());
    }

    void removeWords(List<Word> wordsToBeRemoved){
        words.removeAll(wordsToBeRemoved);
        for (Word w : wordsToBeRemoved){
            wordsMap.remove(w.getKey());
        }
    }
}
