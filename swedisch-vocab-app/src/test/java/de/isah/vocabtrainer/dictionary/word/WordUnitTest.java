package de.isah.vocabtrainer.dictionary.word;

import de.isah.vocabtrainer.dictionary.word.state.IllegalStateTransitionException;
import de.isah.vocabtrainer.dictionary.word.state.WordStateCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateDictionary;
import de.isah.vocabtrainer.dictionary.word.state.WordStateGSCorrect;
import de.isah.vocabtrainer.dictionary.word.state.WordStateIncomplete;
import de.isah.vocabtrainer.dictionary.word.state.WordStateLearn;
import de.isah.vocabtrainer.dictionary.word.state.WordStateNew;
import de.isah.vocabtrainer.dictionary.word.state.WordStateSGCorrect;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WordUnitTest {

    @Test
    public void testNormalChar() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.NONE);

        assertEquals("pojke", word.printSwedishAndGrammar());
        assertEquals("Junge", word.printGerman());
        assertFalse(word.getState() instanceof WordStateSGCorrect);
        assertFalse(word.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testSpecialChar() throws Exception {
        Word word = new Word();
        word.setGerman("äöüßÄÖÜ");
        word.setSwedish("äöåÄÖÅ", WordPrefix.NONE);

        assertEquals("äöåÄÖÅ", word.printSwedishAndGrammar());
        assertEquals("äöüßÄÖÜ", word.printGerman());
    }

    @Test
    public void testIsCorrect() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.NONE);

        word.setState(new WordStateNew());
        word.setState(new WordStateLearn());
        word.setState(new WordStateSGCorrect());
        word.setState(new WordStateCorrect());
        assertTrue(word.getState() instanceof WordStateCorrect);
    }

    @Test
    public void testIsOnList1() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.NONE);

        word.setState(new WordStateNew());
        assertTrue(word.getState() instanceof WordStateNew);
        assertFalse(word.getState() instanceof WordStateLearn);
    }

    @Test
    public void testIsOnList2() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.NONE);

        word.setState(new WordStateNew());
        word.setState(new WordStateLearn());
        assertFalse(word.getState() instanceof WordStateNew);
        assertTrue(word.getState() instanceof WordStateLearn);
    }

    @Test
    public void testTwoGerman() throws Exception {
        Word word = new Word();
        word.setGerman("german1", "german2");

        assertEquals("german1, german2", word.printGerman());
    }

    @Test
    public void testGermanNull() throws Exception {
        Word word = new Word();
        assertEquals("null", word.printGerman());
    }

    @Test(expected = NullPointerException.class)
    public void testSwedishNull() throws Exception {
        Word word = new Word();
        assertEquals("null", word.printSwedishAndGrammar());
    }

    @Test
    public void testGrammar() throws Exception {
        Word word = new Word();
        word.setSwedish("word", WordPrefix.NONE);
        word.setGrammar("grammar1");

        assertEquals("word\ngrammar1", word.printSwedishAndGrammar());
    }

    @Test
    public void testTwoGrammar() throws Exception {
        Word word = new Word();
        word.setSwedish("word", WordPrefix.NONE);
        word.setGrammar("grammar1", "grammar2");

        assertEquals("word\ngrammar1\ngrammar2", word.printSwedishAndGrammar());
    }

    @Test
    public void testRemark() throws Exception {
        Word word = new Word();
        word.setSwedish("word", WordPrefix.NONE);
        word.setRemark("test remark");

        assertEquals("test remark", word.printRemark());
    }

    @Test
    public void testSerializeFullWord(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.NONE);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("none;swedish;german1,german2;grammar1,grammar2;remark;WordStateInitial--",word.serialize());
    }

    @Test
    public void testSerializePartialWord(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.NONE);
        word.setGerman("german1");

        assertEquals("none;swedish;german1;;;WordStateInitial--",word.serialize());
    }

    @Test
    public void testDeserializeFullWord(){
        Word word = new Word("none;swedish;german1,german2;grammar1,grammar2;remark;WordStateLearn");
        assertEquals("none;swedish;german1,german2;grammar1,grammar2;remark;WordStateLearn--",word.serialize());
        assertTrue(word.getState() instanceof WordStateLearn);
    }

    @Test
    public void testDeserializePartialWord(){
        Word word = new Word("none;swedish;german1; ; ;WordStateDictionary");
        assertEquals("none;swedish;german1; ; ;WordStateDictionary--",word.serialize());
    }

    @Test
    public void testTabsEn() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.EN);
        word.setGrammar("pojken","pojkar","pojkarna");

        assertEquals("(en)\n\tpojke\n\tpojken\n\tpojkar\n\tpojkarna", word.printSwedishAndGrammar());
        assertEquals("Junge", word.printGerman());
        assertFalse(word.getState() instanceof WordStateSGCorrect);
        assertFalse(word.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testTabsEtt() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.ETT);
        word.setGrammar("pojken","pojkar","pojkarna");

        assertEquals("(ett)\n\tpojke\n\tpojken\n\tpojkar\n\tpojkarna", word.printSwedishAndGrammar());
        assertEquals("Junge", word.printGerman());
        assertFalse(word.getState() instanceof WordStateSGCorrect);
        assertFalse(word.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testTabsAtt() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.ATT);
        word.setGrammar("pojken","pojkar","pojkarna");

        assertEquals("(att)\n\tpojke\n\tpojken\n\tpojkar\n\tpojkarna", word.printSwedishAndGrammar());
        assertEquals("Junge", word.printGerman());
        assertFalse(word.getState() instanceof WordStateSGCorrect);
        assertFalse(word.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testToString(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.NONE);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("swedish = german1,german2 "+String.valueOf(Character.toChars(0x26A0)),word.toString());
    }

    @Test
    public void testPrintWholeWord() throws IllegalStateTransitionException{
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.NONE);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");
        word.setState(new WordStateDictionary());

        assertEquals("swedish\ngrammar1\ngrammar2\n\ngerman1, german2\n\nremark\n\ndictionary list",word.printWholeWord());
    }

    @Test
    public void testToStringEn(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.EN);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("swedish, en = german1,german2 "+String.valueOf(Character.toChars(0x26A0)),word.toString());
    }

    @Test
    public void testToStringEtt(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.ETT);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("swedish, ett = german1,german2 "+String.valueOf(Character.toChars(0x26A0)),word.toString());
    }

    @Test
    public void testToStringAtt(){
        Word word = new Word();
        word.setSwedish("swedish", WordPrefix.ATT);
        word.setGerman("german1","german2");
        word.setGrammar("grammar1","grammar2");
        word.setRemark("remark");

        assertEquals("swedish, att = german1,german2 "+String.valueOf(Character.toChars(0x26A0)),word.toString());
    }

    @Test
    public void testSwedishWithPrefix() throws Exception {
        Word word = new Word();
        word.setGerman("Junge");
        word.setSwedish("pojke", WordPrefix.EN);

        assertEquals("(en)\n\tpojke", word.printSwedishAndGrammar());
    }

    @Test
    public void testComparatorOne(){
        Word word = new Word();
        Word.WordComparator comparator = word.new WordComparator();
        Word w1 = new Word();
        w1.setSwedish("aaa", WordPrefix.NONE);
        w1.setGerman("aaa");
        Word w2 = new Word();
        w2.setSwedish("bbb", WordPrefix.NONE);
        w2.setGerman("bbb");
        int result = comparator.compare(w1,w2);
        assertTrue(result < 0);
    }

    @Test
    public void testComparatorTwo(){
        Word word = new Word();
        Word.WordComparator comparator = word.new WordComparator();
        Word w1 = new Word();
        w1.setSwedish("eee", WordPrefix.NONE);
        w1.setGerman("eee");
        Word w2 = new Word();
        w2.setSwedish("aaa", WordPrefix.NONE);
        w2.setGerman("aaa");
        int result = comparator.compare(w1,w2);
        assertTrue(result > 0);
    }

    @Test
    public void testComparatorThree(){
        Word word = new Word();
        Word.WordComparator comparator = word.new WordComparator();
        Word w1 = new Word();
        w1.setSwedish("xxx", WordPrefix.NONE);
        w1.setGerman("xxx");
        Word w2 = new Word();
        w2.setSwedish("xxx", WordPrefix.NONE);
        w2.setGerman("xxx");
        int result = comparator.compare(w1,w2);
        assertTrue(result == 0);
    }

    @Test
    public void testComparatorFour(){
        Word word = new Word();
        Word.WordComparator comparator = word.new WordComparator();
        Word w1 = new Word();
        w1.setSwedish("ååå", WordPrefix.NONE);
        w1.setGerman("aaa");
        Word w2 = new Word();
        w2.setSwedish("äää", WordPrefix.NONE);
        w2.setGerman("bbb");
        int result = comparator.compare(w1,w2);
        assertTrue(result < 0);
    }

    @Test
    public void testValidateValid() throws Exception {
        Word word = new Word();
        word.setRemark("The chars -()/., are allowed");
        // no exception is expected, no assert needed
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateGerman() throws Exception {
        Word word = new Word();
        word.setGerman("&<>$%§");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateSwedisch1() throws Exception {
        Word word = new Word();
        word.setSwedish("&<>$%§", WordPrefix.NONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateSwedisch2() throws Exception {
        Word word = new Word();
        word.setSwedish("&<>$%§", WordPrefix.NONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateGrammar() throws Exception {
        Word word = new Word();
        word.setGrammar("&<>$%§");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateConstructor(){
        new Word("&;<,>;$,%;$remark remark;;");
    }

    @Test
    public void testSetStateInitialToNew() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        assertTrue(w.getState() instanceof WordStateNew);
    }

    @Test
    public void testSetStateInitialInvalid() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateDictionary());
        assertTrue(w.getState() instanceof WordStateDictionary);
    }

    @Test
    public void testSetStateIncompleteToNew() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateIncomplete());
        w.setState(new WordStateNew());
        assertTrue(w.getState() instanceof WordStateNew);
    }

    @Test
    public void testSetStateNewToNew()throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateNew());
        assertTrue(w.getState() instanceof WordStateNew);
    }

    @Test
    public void testSetStateNewToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateLearnToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testSetStateLearnToCorrectGS() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        assertTrue(w.getState() instanceof WordStateGSCorrect);
    }

    @Test
    public void testSetStateLearnToCorrectSG() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        assertTrue(w.getState() instanceof WordStateSGCorrect);
    }

    @Test
    public void testSetStateCorrectGSToCorrect() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateCorrect());
        assertTrue(w.getState() instanceof WordStateCorrect);
    }

    @Test
    public void testSetStateCorrectGSToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateCorrectGSToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testSetStateCorrectSGToCorrect() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        w.setState(new WordStateCorrect());
        assertTrue(w.getState() instanceof WordStateCorrect);
    }

    @Test
    public void testSetStateCorrectSGToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateCorrectSGToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testSetStateCorrectToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateCorrect());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateCorrectToDictionary() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateGSCorrect());
        w.setState(new WordStateCorrect());
        w.setState(new WordStateDictionary());
        assertTrue(w.getState() instanceof WordStateDictionary);
    }

    @Test
    public void testSetStateCorrectToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateNew());
        w.setState(new WordStateLearn());
        w.setState(new WordStateSGCorrect());
        w.setState(new WordStateCorrect());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }

    @Test
    public void testSetStateDictionaryToLearn() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateDictionary());
        w.setState(new WordStateLearn());
        assertTrue(w.getState() instanceof WordStateLearn);
    }

    @Test
    public void testSetStateDictionaryToNew() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateDictionary());
        w.setState(new WordStateNew());
        assertTrue(w.getState() instanceof WordStateNew);
    }

    @Test
    public void testSetStateDictionaryToIncomplete() throws IllegalStateTransitionException{
        Word w = new Word();
        w.setState(new WordStateDictionary());
        w.setState(new WordStateIncomplete());
        assertTrue(w.getState() instanceof WordStateIncomplete);
    }
}