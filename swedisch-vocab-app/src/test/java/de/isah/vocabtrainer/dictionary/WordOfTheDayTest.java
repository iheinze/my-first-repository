package de.isah.vocabtrainer.dictionary;

import org.junit.Test;

import de.isah.vocabtrainer.dictionary.word.WordBuilder;
import de.isah.vocabtrainer.dictionary.word.WordPrefix;

import static org.junit.Assert.*;

public class WordOfTheDayTest {

    @Test
    public void testWordIsNull() {
        WordOfTheDay.setWordOfTheDay(null);
        assertEquals("No word of the day chosen", WordOfTheDay.printGerman());
        assertEquals("No word of the day chosen", WordOfTheDay.printSwedishAndGrammar());
        assertEquals("No word of the day chosen", WordOfTheDay.printRemark());
        assertEquals("No word of the day chosen", WordOfTheDay.toStringWidget());
    }

    @Test
    public void testWordFull() {
        WordBuilder builder = new WordBuilder();
        builder.addSwedish("flicka", WordPrefix.EN);
        builder.addGerman("Mädchen");
        builder.addGrammar("flickan", "flickor", "flickorna");
        builder.addRemark("some remark");

        WordOfTheDay.setWordOfTheDay(builder.build());
        assertEquals("Mädchen", WordOfTheDay.printGerman());
        assertEquals("(en)\n" +
                "\tflicka\n" +
                "\tflickan\n" +
                "\tflickor\n" +
                "\tflickorna", WordOfTheDay.printSwedishAndGrammar());
        assertEquals("some remark", WordOfTheDay.printRemark());
        assertEquals("flicka, en = Mädchen", WordOfTheDay.toStringWidget());
    }

    @Test
    public void testWordNoRemark() {
        WordBuilder builder = new WordBuilder();
        builder.addSwedish("flicka", WordPrefix.EN);
        builder.addGerman("Mädchen");
        builder.addGrammar("flickan", "flickor", "flickorna");

        WordOfTheDay.setWordOfTheDay(builder.build());
        assertEquals("Mädchen", WordOfTheDay.printGerman());
        assertEquals("(en)\n" +
                "\tflicka\n" +
                "\tflickan\n" +
                "\tflickor\n" +
                "\tflickorna", WordOfTheDay.printSwedishAndGrammar());
        assertEquals("", WordOfTheDay.printRemark());
        assertEquals("flicka, en = Mädchen", WordOfTheDay.toStringWidget());
    }

}