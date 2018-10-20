package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateDictionary;
import de.isah.vocabtrainer.dictionary.word.state.WordStateGSCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.floor;

/**
 * Created by isa.heinze on 15.06.2018.
 */

public class LearnWordList extends WordList {

    public void shuffle(){
        Collections.shuffle(words);
    }

    public void clearListFromKnownWords(){
        try {
            List<Word> wordsToBeRemoved = new ArrayList<>();

            for (Word w : words) {
                if (w.getState() instanceof WordStateCorrect) {
                    w.setState(new WordStateDictionary());
                    wordsToBeRemoved.add(w);
                }
            }

            words.removeAll(wordsToBeRemoved);
            // TODO: separate LearnWordList from WordList, they seem to be too different to be related
            for (Word w : wordsToBeRemoved){
                wordsMap.remove(w.getKey());
            }
        } catch (IllegalStateTransitionException e){
            // do nothing
        }
    }

    public String printResults(){
        int counterSG = 0;
        int counterGS = 0;

        int size = words.size();

        for(Word w : words){
            if(w.getState() instanceof WordStateSGCorrect || w.getState() instanceof WordStateCorrect){
                counterSG++;
            }
            if(w.getState() instanceof WordStateGSCorrect || w.getState() instanceof WordStateCorrect){
                counterGS++;
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Total Number of Words: ").append(size).append("\n");
        builder.append("Correct (Swedish-German): ").append(counterSG).append(" ").append((generateSmiley(size, counterSG))).append("\n");
        builder.append("Correct (German-Swedish): ").append(counterGS).append(" ").append(generateSmiley(size, counterGS)).append("\n");
        builder.append("\n");
        builder.append(generateMessage(size, counterSG, counterGS));
        return builder.toString();
    }

    private String generateSmiley(int size, int correct){
        if(correct == size){
            return String.valueOf(Character.toChars(0x1F601));
        }

        if(correct >= floor(size*2/3)){
            return String.valueOf(Character.toChars(0x1F603));
        }

        return String.valueOf(Character.toChars(0x1F61E));
    }

    private String generateMessage(int size, int correctSG, int correctGS){

        if(correctSG == size && correctGS == size){
            return "Brilliant " + String.valueOf(Character.toChars(0x1F601));
        }

        if(correctSG >= floor(size*2/3) && correctGS >= floor(size*2/3)){
            return "Good job " + String.valueOf(Character.toChars(0x1F603));
        }

        return "Still some work to do " + String.valueOf(Character.toChars(0x1F61E));
    }
}
