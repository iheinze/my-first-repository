package de.isah.vocabtrainer.dictionary.learn;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import de.isah.vocabtrainer.dictionary.Dictionary;
import de.isah.vocabtrainer.dictionary.DictionaryCache;
import de.isah.vocabtrainer.dictionary.word.Word;
import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateDictionary;
import de.isah.vocabtrainer.dictionary.word.state.WordStateGSCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

import static org.junit.Assert.*;

public class VocabTrainingMessengerTest {

    @Before
    public void setup() {
        DictionaryCache.reloadDictionary("3");
        Dictionary dictionary = DictionaryCache.getCachedDictionary();
        dictionary.createToLearnList(5);
    }

    @Test
    public void testConstructor() {
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
        for (int i = 0; i < 10; i++) {
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

    @Test
    public void testLearnListLength() {
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        assertEquals(5, messenger.giveMeLearnListLength());
    }

    @Test
    public void testSetCorrectStateSG() {
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        messenger.giveMeNextWord();
        messenger.setCorrectState(LearnDirection.SG);
        assertTrue(messenger.giveMeCurrentWord().getState() instanceof WordStateSGCorrect);
    }

    @Test
    public void testSetCorrectStateSG2() throws IllegalStateTransitionException {
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        Word w = messenger.giveMeNextWord();
        w.setCorrectStateSG();
        w.setCorrectStateGS();
        w.setState(new WordStateDictionary());
        messenger.setCorrectState(LearnDirection.SG);
        System.out.println(messenger.giveMeCurrentWord().getState());
        assertTrue(messenger.giveMeCurrentWord().getState() instanceof WordStateDictionary);
    }

    @Test
    public void testSetCorrectStateGS() {
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        messenger.giveMeNextWord();
        messenger.setCorrectState(LearnDirection.GS);
        assertTrue(messenger.giveMeCurrentWord().getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testSetCorrectStateGS2() throws IllegalStateTransitionException {
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        Word w = messenger.giveMeNextWord();
        w.setCorrectStateSG();
        w.setCorrectStateGS();
        w.setState(new WordStateDictionary());
        messenger.setCorrectState(LearnDirection.GS);
        System.out.println(messenger.giveMeCurrentWord().getState());
        assertTrue(messenger.giveMeCurrentWord().getState() instanceof WordStateDictionary);
    }

    @Test
    public void testGiveMeOtherSideButtonNameSG(){
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        assertEquals("Show German", messenger.giveMeOtherSideButtonName(LearnDirection.SG));
    }

    @Test
    public void testGiveMeOtherSideButtonNameGS(){
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        assertEquals("Show Swedish", messenger.giveMeOtherSideButtonName(LearnDirection.GS));
    }

    @Test
    public void testGiveMeFirstSideSG(){
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        messenger.giveMeNextWord();
        String result = messenger.giveMeFirstSide(LearnDirection.SG);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGiveMeFirstSideGS(){
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        messenger.giveMeNextWord();
        String result = messenger.giveMeFirstSide(LearnDirection.GS);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGiveMeSecondSideSG(){
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        messenger.giveMeNextWord();
        String result = messenger.giveMeSecondSide(LearnDirection.SG);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGiveMeSecondSideGS(){
        VocabTrainingMessenger messenger = new VocabTrainingMessenger();
        messenger.giveMeNextWord();
        String result = messenger.giveMeSecondSide(LearnDirection.GS);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}