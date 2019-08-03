package de.isah.vocabtrainer.dictionary;

import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;

public class WordOfTheDay {

    // TODO: has to be a singelton, must be set to different word every now and then
    private Word wordOfTheDay;

    public WordOfTheDay(){
        WordBuilder builder = new WordBuilder();
        builder.addSwedish("flicka", WordPrefix.EN);
        builder.addGerman("Mädchen");
        builder.addGrammar("flickan", "flickor", "flickorna");
        this.wordOfTheDay = builder.build();
    }

    public String printSwedishAndGrammar(){
        return this.wordOfTheDay.printSwedishAndGrammar();
    }

    public String printGerman(){
        return this.wordOfTheDay.printGerman();
    }

    public String printRemark(){
        return this.wordOfTheDay.printRemark();
    }

    @Override
    public String toString(){
        return "Word of the day:\n"+this.wordOfTheDay.toStringMinimal();
    }
}
