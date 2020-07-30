package de.isah.vocabtrainer.dictionary;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

import de.isah.vocabtrainer.dictionary.word.Word;

import static org.junit.Assert.*;

public class VocabTrainingMessengerTest {

    @BeforeClass
    public static void setup(){
        DictionaryCache.reloadDictionary("3");
        Dictionary dictionary = DictionaryCache.getCachedDictionary();
        dictionary.createToLearnList(5);
    }

    @Test
    public void testConstructor(){
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        assertEquals(0, messenger.giveMeCurrentCounter());
        assertNull(messenger.giveMeCurrentWord());
    }

    @Test
    public void testFirstIteration() throws JSONException {
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        Word word = messenger.giveMeNextWord();
        assertEquals(1, messenger.giveMeCurrentCounter());
        assertNotNull(messenger.giveMeCurrentWord());
        assertEquals(word.serializeToJsonString(), messenger.giveMeCurrentWord().serializeToJsonString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIterateTillEnd() throws JSONException {
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        for(int i = 0; i < 10; i++) {
            messenger.giveMeNextWord();
        }
    }

    @Test
    public void testReset() throws JSONException {
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        messenger.giveMeNextWord();
        messenger.reset();
        assertEquals(0, messenger.giveMeCurrentCounter());
        assertNull(messenger.giveMeCurrentWord());
    }
}